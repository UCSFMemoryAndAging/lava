package edu.ucsf.lava.core.util;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class LavaDateUserType implements UserType {

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deepCopy(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode(Object x) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	public Class returnedClass() {
		return LavaDate.class;
	}

	public int[] sqlTypes() {
		return new int[]{java.sql.Types.TIMESTAMP};
	}



}
