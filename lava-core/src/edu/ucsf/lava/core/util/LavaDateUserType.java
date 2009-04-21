package edu.ucsf.lava.core.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class LavaDateUserType implements UserType, Serializable {

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return (LavaDate) deepCopy(cached);
	
	}

	
	public Object deepCopy(Object value) throws HibernateException {
	
		if(value==null){return value;}
		
		
		//Not sure if we need to do all this, but I suppose it doesn't hurt. 
		if(LavaDate.class.isAssignableFrom(value.getClass())){
			return new LavaDate(((LavaDate)value).getTimestamp());
		}else if(Date.class.isAssignableFrom(value.getClass())){
			return new LavaDate(((Date)value).getTime());
		}else if(java.sql.Date.class.isAssignableFrom(value.getClass())){
			return new LavaDate(((java.sql.Date)value).getTime());
		}else if(Timestamp.class.isAssignableFrom(value.getClass())){
			return new LavaDate(((Timestamp)value).getTime());
		}else{
			return null;
		}
	}

	
	public Serializable disassemble(Object value) throws HibernateException {
		return (LavaDate) deepCopy(value);
	}

	/**
	 * LavaDate equals override checks inner dates for equality
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x != null){
			return x.equals(y);
		}
		return false;
	}

	public int hashCode(Object x) throws HibernateException {
		if(x != null){
			return x.hashCode();
		}else{
			return 0;
		}
	}

	public boolean isMutable() {
		return true;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		return new LavaDate(rs.getTimestamp(names[0]));		
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if(value!=null && LavaDate.class.isAssignableFrom(value.getClass())){
			LavaDate lavaDate = (LavaDate)value;
			st.setTimestamp(index,lavaDate.getTimestamp());
		}else{
			st.setTimestamp(index,null);
		}
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return (LavaDate) deepCopy(original);
	}

	public Class returnedClass() {
		return LavaDate.class;
	}

	public int[] sqlTypes() {
		return new int[]{java.sql.Types.TIMESTAMP};
	}



}
