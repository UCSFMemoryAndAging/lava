/**
 * Feb 23, 2005
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
package net.sf.uitags.testdata;

import java.util.ArrayList;
import java.util.List;

public class Continent {
  private String id;
  private String name;
  private List countries;

  public Continent(String id, String name) {
    this.id = id;
    this.name = name;
    this.countries = new ArrayList();
  }

  public void setId(String value) { this.id = value; }
  public String getId() { return this.id; }

  public void setName(String value) { this.name = value; }
  public String getName() { return this.name; }

  public void addCountry(Country country) {
    this.countries.add(country);
    country.setContinent(this);
  }
  public List getCountries() { return this.countries; }
}
