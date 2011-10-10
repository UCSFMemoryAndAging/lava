package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;

public class NamedQueryHibernateCallback extends LavaHibernateCallback {

		private String namedQuery;
		private Query query;
		
	public NamedQueryHibernateCallback(String namedQuery,  LavaDaoFilter filter){
		super(filter);
		this.namedQuery = namedQuery;
		
	}
	
	public void getResultsCount(){
		
	}
	public Object doQuery() throws HibernateException,
			SQLException {
		
		
		if(filter.rowSelectorsSet()){
			return this.getResultsByRowSelectors(query.scroll());
		}else{
			return query.list();
		}
	}


	protected void applyParameter(LavaDaoParam param) {
		if(param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_NAMED)){
			DaoHibernateNamedParam namedParam = (DaoHibernateNamedParam)param;
			query.setParameter(namedParam.getParamName(),namedParam.getParamValue());
		}else if (param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_POSITIONAL)){
			DaoHibernatePositionalParam posParam = (DaoHibernatePositionalParam)param;
			query.setParameter(posParam.getParamPos(),posParam.getParamValue());
		}else{
			logger.warn("Invalid LavaDaoParam type:" + param.getType() + " passed to a NamedQuery");
		}
		
		/* alternatively may need to do something like this
		 * 
		 * 
		 * 	if (paramTypes[i] == String.class) {
					query.setString(paramNames[i], (String) paramValues[i]);
				}
				else if (paramTypes[i] == Short.class) {
					query.setShort(paramNames[i], (Short) paramValues[i]);
				}
				else if (paramTypes[i] == Integer.class) {
					query.setInteger(paramNames[i], (Integer) paramValues[i]);
				}
				else if (paramTypes[i] == Long.class) {
					query.setLong(paramNames[i], (Long) paramValues[i]);
				}
				else if (paramTypes[i] == Float.class) {
					query.setFloat(paramNames[i], (Float) paramValues[i]);
				}
				else if (paramTypes[i] == Date.class) {
					query.setDate(paramNames[i], (Date) paramValues[i]);
				}		
				else if (paramTypes[i] == Timestamp.class) {
					query.setTimestamp(paramNames[i], (Timestamp) paramValues[i]);
				}		
		 * 
		 * 
		 */
		
	}

	protected void applySort(String Property, Boolean Ascending) {
	//do nothing as named queries have not custom sort options,,,	
		return;
	}


	protected void initQuery() {
		query = session.getNamedQuery(namedQuery);
		
	}

	@Override
	protected void applyAlias(String collection, String alias) {
		//no aliases in named queries
		return;
	}
	
	protected void applyOuterAlias(String collection, String alias) {
		//no aliases in named queries
		return;
	}
	protected void applyProjections() {
		//do nothing as hql queries do not have custom projections	
			return;
	}
	
}

