package de.oganisyan.geo.util;

import android.location.*;
import android.content.*;
import java.util.*;
import android.util.*;
import android.widget.*;
import java.io.*;
import de.oganisyan.geo.*;

public class Parser
{
    private static double getCoordinat(final String s) {
        final int index = s.indexOf(":");
        final int lastIndex = s.lastIndexOf(":");
        double n;
        if (index == lastIndex) {
            n = Integer.valueOf(s.substring(0, index)) + Double.valueOf(s.substring(index + 1)) / 60.0;
        }
        else {
            n = Integer.valueOf(s.substring(0, index)) + Integer.valueOf(s.substring(index + 1, lastIndex)) / 60.0 + Double.valueOf(s.substring(lastIndex + 1)) / 3600.0;
        }
        return n;
    }
    
   /* private static AirspaceSegment getOpenAirClockWiseDA(final String s, final boolean b, final Point point) throws AirspaceException {
        final int index = s.indexOf(44);
        final int lastIndex = s.lastIndexOf(44);
        final double n = 1852.216 * Double.valueOf(s.substring(0, index));
        return new AirspaceSegment(getPoint(point, Double.valueOf(s.substring(index + 1, lastIndex)), n), getPoint(point, Double.valueOf(s.substring(lastIndex + 1)), n), point, b);
    }
    
    private static AirspaceSegment getOpenAirClockWiseDB(final String s, final boolean b, final Point point) throws AirspaceException {
        final int index = s.indexOf(",");
        return new AirspaceSegment(getOpenAirPoint(s.substring(0, index).trim()), getOpenAirPoint(s.substring(index + 1).trim()), point, b);
    }
*/
    private static Point getOpenAirPoint(final String s) {
        int n;
        if (s.indexOf("N") < 0) {
            n = s.indexOf("S");
        }
        else {
            n = s.indexOf("N");
        }
        int n2;
        if (s.indexOf("W") < 0) {
            n2 = s.indexOf("E");
        }
        else {
            n2 = s.indexOf("W");
        }
        final String trim = s.substring(0, n).trim();
        final String trim2 = s.substring(n + 1, n2).trim();
        double n3;
        if (s.charAt(n) == 'N') {
            n3 = 1.0;
        }
        else {
            n3 = -1.0;
        }
        double n4;
        if (s.charAt(n2) == 'E') {
            n4 = 1.0;
        }
        else {
            n4 = -1.0;
        }
        return new Point(n3 * getCoordinat(trim), n4 * getCoordinat(trim2));
    }
    
    private static Point getPoint(final Point point, final double n, final double n2) {
        final float[] array = new float[2];
        Location.distanceBetween(point.getLatitude(), point.getLongitude() - 0.5, point.getLatitude(), 0.5 + point.getLongitude(), array);
        final double n3 = array[0];
        Location.distanceBetween(point.getLatitude() - 0.5, point.getLongitude(), 0.5 + point.getLatitude(), point.getLongitude(), array);
        return new Point(n2 * Math.cos(3.141592653589793 * n / 180.0) / array[0] + point.getLatitude(), n2 * Math.sin(3.141592653589793 * n / 180.0) / n3 + point.getLongitude());
    }
    
    private static BufferedReader getResourceReader(final Context context, final int n) {
        return new BufferedReader(new InputStreamReader(context.getResources().openRawResource(n)));
    }
    
/*    public static ArrayList<Airspace> openAir(final Context context, final int n, final ArrayList<Airspace> list) {
        String s = context.getResources().getResourceName(n);
        final int lastIndex = s.lastIndexOf(47);
        if (lastIndex >= 0) {
            s = s.substring(lastIndex + 1);
        }
        return openAir(context, s, getResourceReader(context, n), list);
    }
    
    public static ArrayList<Airspace> openAir(final Context context, final File file, ArrayList<Airspace> openAir) {
        try {
            openAir = openAir(context, file.getName(), new BufferedReader(new InputStreamReader(new FileInputStream(file))), openAir);
            return openAir;
        }
        catch (FileNotFoundException ex) {
            Log.i("OpenAirParser", "File " + file.getName() + ": " + ex.getMessage());
            Toast.makeText(context, (CharSequence)("File " + file.getName() + ": " + ex.getMessage()), Toast.LENGTH_SHORT).show();
            return openAir;
        }
    }
      
    //TODO openAir - Ver se funcionou
    private static ArrayList<Airspace> openAir(Context context, String sourceName, BufferedReader in, ArrayList<Airspace> arg18) {
    	float v9;
    	String v7;
    	StringBuffer v4 = new StringBuffer();
    	Point ponto = null;
    	boolean v6 = true;
    	int linha = 0;
    	AirspaceHelper v2 = null;
    	AirspaceHelper v1 = null;
    	while(true){
    		try{
    			v7 = in.readLine();
    			if(v7 == null) 
    				break;
    			v7 = v7.toUpperCase(Locale.ENGLISH);
    			if(!v7.startsWith("*")) {
    				if(v7.startsWith("AC")) 
    				{
    					if(v2 != null) {       				
    						v2.close();
    						if(AirspaceFilter.instanceOf().use(((Airspace)v2))) {
    							arg18.add(v2);
    						}
    					}                         
    					v1 = new AirspaceHelper(sourceName, v7.substring(2).trim());
    					v6 = true;                                                        				
    				}
    				else if((v2 == null)||(v7.startsWith("SP"))||(v7.startsWith("SB"))||(v7.startsWith("AT")))
    				{
    					v1=v2;
    				}
    				else if(v7.startsWith("AN")) 
    				{
    					v2.setName(v7.substring(2).trim());
    					v1 = v2;
    				}
    				else if(v7.startsWith("AL")) 
    				{
    					v2.setBase(v7.substring(2).trim());
    					v1 = v2;
    				}
    				else if(!v7.startsWith("AH")) 
    				{
    					v2.setTops(v7.substring(2).trim());
    					v1 = v2;
    				}    				
    				else if(v7.startsWith("V"))
    				{
    					String v10 = v7.substring(2).trim();
    					if(v10.startsWith("X=")) 
    					{
    						ponto = Parser.getOpenAirPoint(v10.substring(2).trim());
    						v1 = v2;        		            
    					}
    					else 
    					{
    						if(v10.startsWith("D=")) 
    						{
    							if(v10.charAt(2) != 45) 
    							{
    								v6 = true;
    								v1 = v2;
    							}
    							else
    							{        						 
    								v6 = false;
    								v1 = v2;
    							}
    						}
    						else
    						{
    							v1 = v2;
    						}
    					}        			
    				}
    				else if(!v7.startsWith("DC")) 
    				{
    					v9 = Float.valueOf(v7.substring(2).trim()).floatValue() * 1852.215942f;
    					if(ponto == null) {
    						throw new AirspaceException("missing Variable assignment at line: " + linha + " | " + v7); 
    					}
    					v1 = new AirspaceHelper(((Airspace)v2), ponto, v9);
    				}
    				else if(v7.startsWith("DP")) 
    				{
    					v2.addPoint(Parser.getOpenAirPoint(v7.substring(2).trim()));
    					v1 = v2;     		               
    				}
    				else if(v7.startsWith("DA"))
    				{
    					if(ponto == null) {
    						throw new AirspaceException("missing Variable assignment at line: " + linha + " | " + v7);
    					}

    					v2.addSegment(Parser.getOpenAirClockWiseDA(v7.substring(2).trim(), v6, ponto));
    					v1 = v2;
    				}
    				else if(!v7.startsWith("DB"))
    				{
    					if(ponto == null) 
    					{
    						throw new AirspaceException("missing Variable assignment at line: " + linha + " | " + v7);   
    					}

    					v2.addSegment(Parser.getOpenAirClockWiseDB(v7.substring(2).trim(), v6, ponto));
    					v1 = v2;        			 
    				}
    				else
    				{	
    					if(v7.startsWith("END"))        		         
    						break;

    					if(v7.trim().length() == 0) {
    						v1 = v2;
    					}
    					else	        		         
    						throw new AirspaceException("Unknown line: " + linha + " | " + v7);
    				}
    			}    	
    		}
    		catch(IOException v5) {
    			v4.append("IOException: " + v5.getMessage() + "\n");
    		}
    		catch(Exception v5_1) {
    			v4.append("Parser error at line " + linha + ": " + v5_1.getMessage() + "\n");
    			v1 = null;
    		}

    		++linha;
    		v2 = v1; 
    	}  

    	if(v2 == null) {

    		if(v4.length() == 0) {
    			return arg18;
    		}
    		Log.i("OpenAirParser", "File " + sourceName + "\n" + v4.toString());
    		Toast.makeText(context, "File " + sourceName + "\n" + v4.toString(), Toast.LENGTH_SHORT).show();
    		return arg18;
    	}
    	try {
			v2.close();
		}
        catch(AirspaceException v5_2) {
           	v4.append("Parser error at end of file : " + v5_2.getMessage() + "\n");
        }
            
    	if(!AirspaceFilter.instanceOf().use(((Airspace)v2))) {

    		if(v4.length() == 0) {
    			return arg18;
    		}
    		Log.i("OpenAirParser", "File " + sourceName + "\n" + v4.toString());
    		Toast.makeText(context, "File " + sourceName + "\n" + v4.toString(), Toast.LENGTH_SHORT).show();
    		return arg18;
    	}

    	arg18.add(v2);

    	if(v4.length() == 0) {
    		return arg18;
    	}
    	Log.i("OpenAirParser", "File " + sourceName + "\n" + v4.toString());
    	Toast.makeText(context, "File " + sourceName + "\n" + v4.toString(), Toast.LENGTH_SHORT).show();
    	return arg18;
    }        
    
    private static class AirspaceHelper extends Airspace
    {
        protected double east;
        private Point end;
        protected double north;
        protected double south;
        private Point start;
        protected double west;
        
        public AirspaceHelper(final Airspace airspace, final Point center, final float radius) {
            super(airspace.getSourceName(), airspace.getAirspaceClass());
            this.start = null;
            this.end = null;
            this.west = Double.NaN;
            this.east = Double.NaN;
            this.south = Double.NaN;
            this.north = Double.NaN;
            this.setName(airspace.getName());
            this.setBase(airspace.getBase());
            this.setTops(airspace.getTops());
            this.setCircle(true);
            this.setCenter(center);
            this.setRadius(radius);
        }
        
        public AirspaceHelper(final String s, final AirspaceClass airspaceClass) {
            super(s, airspaceClass);
            this.start = null;
            this.end = null;
            this.west = Double.NaN;
            this.east = Double.NaN;
            this.south = Double.NaN;
            this.north = Double.NaN;
        }
        
        public AirspaceHelper(final String s, final String s2) {
            this(s, AirspaceClass.get(s2));
        }
        
        public void addPoint(final Point point) throws AirspaceException {
            if (this.start == null) {
                this.start = point;
                this.end = point;
            }
            else {
                this.addSegment(new AirspaceSegment(this.end, point));
            }
        }
        
        public void addSegment(final AirspaceSegment airspaceSegment) {
            if (this.start == null) {
                this.start = airspaceSegment.getFrom();
                this.end = airspaceSegment.getTo();
            }
            else if (this.end.distanceTo((Location)airspaceSegment.getFrom()) != 0.0f) {
                this.addSegment(new AirspaceSegment(this.end, airspaceSegment.getFrom()));
            }
            this.end = airspaceSegment.getTo();
            final double max = Math.max(airspaceSegment.getFrom().getLatitude(), airspaceSegment.getTo().getLatitude());
            final double min = Math.min(airspaceSegment.getFrom().getLatitude(), airspaceSegment.getTo().getLatitude());
            final double max2 = Math.max(airspaceSegment.getFrom().getLongitude(), airspaceSegment.getTo().getLongitude());
            final double min2 = Math.min(airspaceSegment.getFrom().getLongitude(), airspaceSegment.getTo().getLongitude());
            if (Double.isNaN(this.north) || max > this.north) {
                this.north = max;
            }
            if (Double.isNaN(this.south) || min < this.south) {
                this.south = min;
            }
            if (Double.isNaN(this.east) || max2 > this.east) {
                this.east = max2;
            }
            if (Double.isNaN(this.west) || min2 < this.west) {
                this.west = min2;
            }
            this.getSegments().add(airspaceSegment);
        }
        
        public void close() throws AirspaceException {
            if (!this.isCircle()) {
                if (this.start != null && this.end != null && this.end.distanceTo((Location)this.start) != 0.0f) {
                    this.getSegments().add(new AirspaceSegment(this.end, this.start));
                }
                this.setCenter(new Point((this.north + this.south) / 2.0, (this.west + this.east) / 2.0));
                this.setRadius(this.getCenter().distanceTo((Location)new Point(this.north, this.west)));
            }
        }
        
        public void setBase(final String s) throws AirspaceException {
            this.setBase(Altitude.parser(s));
        }
        
        public void setTops(final String s) throws AirspaceException {
            this.setTops(Altitude.parser(s));
        }
    }*/
}
