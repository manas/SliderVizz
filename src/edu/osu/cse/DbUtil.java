/****Copyright (c) <2012> <Manas Agrawal>.
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>.  The name of the
University may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.*****/


package edu.osu.cse;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import edu.osu.cse.json.*;


public class DbUtil
{
    public String getHistogram(String query,String attr)
    {
        Connection conn = null;
        ResultSet rs=null;
      //  System.out.println("Here Inside function");
        try
        {
            String userName = "root";
            String password = "mylife";
            String url = "jdbc:mysql://localhost:3306/scrolled";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
            //System.out.println ("Database connection established");
            Statement s = conn.createStatement();
          
            s.executeQuery(query);
           // s.executeQuery("SELECT iddept,avg(salary),count(idemployee) FROM scrolled.employee where iddept=2 AND age>35;");

            rs = s.getResultSet();
            System.out.println(attr);
            return convertResultSetToJSON(rs,attr).toString();

           
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error";
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    //System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
        
    }
    
    public JSONArray convertResultSetToJSON(ResultSet rs,String attrColName){

        JSONArray json = new JSONArray();

        try {

             ResultSetMetaData rsmd = rs.getMetaData();
             int cntr = rs.getRow();
             int numColumns = rsmd.getColumnCount();
             CMSketch cm = new CMSketch();
             int count = 0;
             int cnt=0;
             String attrVal = "";
             double ab[];
             String count_col ="";
             ab = new double[cntr];
    
             while(rs.next()){
                
                 JSONObject obj = new JSONObject();

                 for (int i=1; i<numColumns+1; i++) {

                     String column_name = rsmd.getColumnName(i);
                     

                     //not necessary right now but still keeping these.
                     if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
                         obj.put(column_name, rs.getArray(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
                         obj.put(column_name, rs.getBoolean(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
                         obj.put(column_name, rs.getBlob(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
                         obj.put(column_name, rs.getDouble(column_name)); 
                         if (column_name.equalsIgnoreCase(attrColName)){
                        	 attrVal = Double.toString((rs.getDouble(column_name)));
                        	 //System.out.println(attrVal+"Inside rsmd double");
                        	
                        	 
                         }
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
                         obj.put(column_name, rs.getFloat(column_name));
                         if (column_name.equalsIgnoreCase(attrColName)){
                        	 attrVal = Float.toString((rs.getFloat(column_name)));
                        	// System.out.println(attrVal+"Inside rsmd float");
                         }
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
                         obj.put(column_name, rs.getInt(column_name));
                        // System.out.println(column_name);
                         if (column_name.equalsIgnoreCase(attrColName)){
                        	 attrVal = Integer.toString((rs.getInt(column_name)));
                        	 //System.out.println(attrVal+" Inside rsmd");
                         }
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
                         obj.put(column_name, rs.getNString(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
                         obj.put(column_name, rs.getString(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
                         obj.put(column_name, rs.getInt(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
                         obj.put(column_name, rs.getDate(column_name));
                     }
                     else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
                        obj.put(column_name, rs.getTimestamp(column_name));   
                     }
                     else{
                         obj.put(column_name, rs.getObject(column_name));
                     } 
                     
                     if (column_name.contains("count") || column_name.contains("COUNT")){
                    	 count = rs.getInt(column_name);
                    	 count_col = column_name;
                    	 cm.populate(attrVal,count);
                     }

                    }//end foreach
                // 
                 cnt++;
                 json.put(obj);

             }//end while
            /* int[][] a = cm.getMatrix();
             for(int i=0;i<a.length;i++){
            	 for(int j=0;j<a[i].length;j++){
            	 System.out.print(a[i][j]+" ");
            	 }
            	 System.out.println("");
             }*/
             
            	 
        
             System.out.println("Till the end");
             for(int i=0;i<json.length();i++){
            	System.out.println(json.getJSONObject(i).getInt(count_col)+"--"+cm.estimate(Double.toString(json.getJSONObject(i).getDouble(attrColName))));
             }
             //System.out.println(cm.getMatrix().toString());
             

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return json;
    }
    
}
