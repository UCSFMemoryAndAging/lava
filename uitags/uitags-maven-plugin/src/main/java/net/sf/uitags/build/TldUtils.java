/**
 * Sep 5, 2006
 *
 * Copyright 2004 uitags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.uitags.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class TldUtils {
  static Document getDocument(String filename) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setCoalescing(true);
    factory.setIgnoringElementContentWhitespace(true);

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      try {
        return builder.parse(new File(filename));
      }
      catch (SAXException e) {
        throw new RuntimeException(e);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  private static Node[] findDirectTagsByName(Node rootNode, String tagName) {
    List resultNodes = new ArrayList();
    NodeList childNodes = rootNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); ++i) {
      Node currentNode = childNodes.item(i);
      if (currentNode.getNodeName().equals(tagName)) {
        resultNodes.add(currentNode);
      }
    }
    return (Node[]) resultNodes.toArray(new Node[resultNodes.size()]);
  }

  static Map extractTags(Document tldDocument, Document elTldDocument) {
    Map tags = new LinkedHashMap();
    NodeList tagNodes = tldDocument.getElementsByTagName(Tag.TAGNAME_TAG);
    for (int i = 0; i < tagNodes.getLength(); ++i) {
      Tag tag = constructTag(tagNodes.item(i));
      tags.put(tag.getName(), tag);
    }

    tagNodes = elTldDocument.getElementsByTagName(Tag.TAGNAME_TAG);
    for (int i = 0; i < tagNodes.getLength(); ++i) {
      Node node = tagNodes.item(i);
      Tag tag = (Tag) tags.get(getTagName(node));
      tag.setElHandlerName(getTagClassName(node));
    }

    return tags;
  }

  static Map extractTags(Document tldDocument) {
    Map tags = new LinkedHashMap();
    NodeList tagNodes = tldDocument.getElementsByTagName(Tag.TAGNAME_TAG);
    for (int i = 0; i < tagNodes.getLength(); ++i) {
      Tag tag = constructTag(tagNodes.item(i));
      tags.put(tag.getName(), tag);
    }

    return tags;
  }

  private static Tag constructTag(Node node) {
    Tag tag = new Tag(getTagName(node));
    tag.setHandlerName(getTagClassName(node));

    Node[] attributeNodes = findDirectTagsByName(node, Tag.TAGNAME_ATTRIBUTE);
    for (int i = 0; i < attributeNodes.length; ++i) {
      tag.addAttribute(constructAttribute(attributeNodes[i]));
    }

    return tag;
  }

  private static String getValueOf(String tagName, Node rootNode) {
    return findDirectTagsByName(rootNode,
        tagName)[0].getFirstChild().getNodeValue();
  }

  private static String getTagName(Node tagNode) {
    return getValueOf(Tag.TAGNAME_NAME, tagNode);
  }

  private static String getTagClassName(Node tagNode) {
    return getValueOf(Tag.TAGNAME_HANDLER, tagNode);
  }

  private static Attribute constructAttribute(Node node) {
    Attribute attribute = new Attribute(getAttributeName(node));
    attribute.setType(getAttributeType(node));
    return attribute;
  }

  private static String getAttributeName(Node tagNode) {
    return getValueOf(Tag.TAGNAME_NAME, tagNode);
  }

  private static String getAttributeType(Node tagNode) {
    try {
      return getValueOf(Tag.TAGNAME_TYPE, tagNode);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      // return the default value if this optional property was
      // not specified
      return Attribute.DEFAULT_TYPE;
    }
  }


  ///////////////////////////////////
  ////////// Inner Classes //////////
  ///////////////////////////////////

  // made public to allow access from VM template
  public static class Attribute {
    private static final String DEFAULT_TYPE = "java.lang.String";
    private static final String GENERIC_TYPE = "java.lang.Object";

    private String name;
    private String type;
    private static Map predefinedEvaluators;

    static {
      predefinedEvaluators = new HashMap();
      predefinedEvaluators.put("int", "toIntegerValue");
      predefinedEvaluators.put("boolean", "toBooleanValue");
      predefinedEvaluators.put(DEFAULT_TYPE, "toString");
    }

    private Attribute(String name) {
      this.name = name;
    }

    private void setType(String type) {
      this.type = type;
    }

    // the following are made public to allow access from VM template

    public String getName() {
      return this.name;
    }

    public String getHandlerName() {
      return "set" + Character.toUpperCase(
          this.name.charAt(0)) + this.name.substring(1);
    }

    public String getEvaluatorName() {
      String evaluator = (String) predefinedEvaluators.get(this.type);
      if (evaluator == null) {
        evaluator = "toObject";
      }
      return evaluator;
    }

    public String getCastTypeName() {
      if (GENERIC_TYPE.equals(getCustomClassName())) {
        return null;
      }
      return getCustomClassName();
    }

    public String getCustomClassName() {
      if (predefinedEvaluators.containsKey(this.type)) {
        return null;
      }
      return this.type;
    }
  }

  static class Tag {
    private static final String TAGNAME_TAG = "tag";
    private static final String TAGNAME_NAME = "name";
    private static final String TAGNAME_TYPE = "type";
    private static final String TAGNAME_HANDLER = "tag-class";
    private static final String TAGNAME_ATTRIBUTE = "attribute";

    private String name;
    private String tagHandlerName;
    private String elTagHandlerName;
    private List attributeList;

    private Tag(String name) {
      this.name = name;
      this.attributeList = new ArrayList();
    }

    private void setHandlerName(String name) {
      this.tagHandlerName = name;
    }

    private void setElHandlerName(String name) {
      this.elTagHandlerName = name;
    }

    private void addAttribute(Attribute attribute) {
      this.attributeList.add(attribute);
    }

    String getName() {
      return this.name;
    }

    List getAttributes() {
      return this.attributeList;
    }

    String getFullyQualifiedHandlerName() {
      return this.tagHandlerName;
    }

    String getFullyQualifiedElHandlerName() {
      return this.elTagHandlerName;
    }

    String getHandlerPath() {
      return this.tagHandlerName.replace('.', '/');
    }

    String getElHandlerPath() {
      return this.elTagHandlerName.replace('.', '/');
    }

    String getHandlerNameFraction() {
      return getClassNameFraction(this.tagHandlerName);
    }

    String getElHandlerNameFraction() {
      return getClassNameFraction(this.elTagHandlerName);
    }

    String getPackageNameFraction() {
      return getPackageNameFraction(this.tagHandlerName);
    }

    String getElPackageNameFraction() {
      return getPackageNameFraction(this.elTagHandlerName);
    }

    private String getClassNameFraction(String classFullname) {
      int startIndex = classFullname.lastIndexOf('.') + 1;
      return classFullname.substring(startIndex);
    }

    private String getPackageNameFraction(String classFullname) {
      int endIndex = classFullname.lastIndexOf('.') + 1;
      return classFullname.substring(0, endIndex - 1);
    }
  }
}
