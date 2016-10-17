package com.hp.xo.utils.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class CollectionUtil {
	/**批量生成in条件的标识*/
	public static final int BATCH_CONDITON_IN_FLAG = 0;
	/**批量生成or条件的标识*/
	public static final int BATCH_CONDITON_OR_FLAG = 1;
	
	public static <T> T[]  list2Array(List<T> oList) {  
        Object[] oArray=oList.toArray(new Object[] {});  
		T[] ts =(T[]) oArray;
       return ts;  
    }  
 
   public static <T> T[]  set2Array(Set<T> oSet) {  
        Object[] oArray=oSet.toArray(new Object[] {}); 
        T[] ts =(T[]) oArray;
       return ts;  
    }  
 
   public static List<?> set2List(Set<?> oSet) {  
        List<?> tList=new ArrayList(oSet);   
       return tList;  
    }  
 
   public static List<?> array2List(Object[]  tArray) {  
        List<?> tList=Arrays.asList(tArray);         
       return tList;  
    }  
 
   public static Set setList2Set(List<?> tList) {  
       Set<?> tSet=new HashSet(tList);    
       return tSet;  
    }  
 
   public static Set<?> setArray2Set(Object[]  tArray) {  
        Set<?> tSet=new HashSet(Arrays.asList(tArray));  
       return tSet;  
    }
   /**
	 * 批量生成条件
	 * @param arrData 条件数据
	 * @param inor_flag  0 生成格式为in的条件 1生成or的条件
	 * @param field 条件字段
	 * @return
	 */
	public static String getBatchCondition(String[] arrData,String field,int inor_flag){
		if(arrData==null || arrData.length<1) return " ";
		StringBuffer condition = new StringBuffer();
		if(inor_flag==BATCH_CONDITON_IN_FLAG){
			for(String data:arrData){				
				condition.append("'"+data+"',");			
			}
			return " and "+ field + " in (" + condition.deleteCharAt(condition.length()-1) + ") ";
		}else if(inor_flag==BATCH_CONDITON_OR_FLAG){
			for(String data:arrData){		
				condition.append(field+"='"+data.toString()+"' or ");				
			}
			return " and (" + condition.delete(condition.length()-4, condition.length()-1) + ") ";			
		}
		return " ";		
	}
	public static List<?> getSubListByPage(List<?> list,int pageNumber,int pageSize){			
		int start=0;
		int end=0;
		
		if (pageNumber >= 0) {
			start= pageSize * (pageNumber - 1);
			
		}
		if (pageSize > 0) {
			end=start+pageSize;
		}else{
			end=start;
		}
		if(list.size()<end+1){
			end=list.size()-1;
		}
		end=end<0?0:end;
		List<?> subList= list.subList(start, end);
		return subList;
		
	}
}
