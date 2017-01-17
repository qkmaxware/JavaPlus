/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plus.math;

/**
 *
 * @author Colin Halseth
 */
public class ConversionTools {
    
    public static enum Distance{
        nm(1000000000), mm(1000), cm(100), dm(10), m(1), km(0.001), Mm(0.000001), Gm(0.000000001), 
        league(0.0002071), mile(0.0006214), yard(1.094), foot(3.281), inch(39.37),
        fathom(0.5468);
        
        private double conversion;
        Distance(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Area{
        mm(1000000), cm(10000), m(1), km(0.000001),
        mile(3.861021585425E-7), yard(1.195990046301),
        acre(0.0002471053814672), hectare(0.0001); 
        
        private double conversion;
        Area(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Volume{
        mm(1000000000), cm(1000000), dm(1000), m(1), km(0.000000001), 
        ml(1000000), cl(100000), litre(1000),
        gallon(264.2), quart(1057), pint(2113), gill(8454), ounce(33810),
        cup(4167), tablespoon(66670), teaspoon(200000),
        mile(0.0000000002399), yard(1.308), foot(35.31), inch(61020); 
        
        private double conversion;
        Volume(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Mass{
        amu(6.022e+26), mg(1000000), g(1000), kg(1), tonne(0.001), kilotonne(0.000001),
        ounce(35.27), pound(2.205); 
        
        private double conversion;
        Mass(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Speed{
        m_s(1), km_hr(3.6), km_s(0.001), m_min(60), mph(2.237), ft_s(3.281), ft_min(196.9),
        knot(1.944), c(0.000000003336); 
        
        private double conversion;
        Speed(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Temperature{
        celsius(1), kelvin(274.1), fahrenheit(35.6); 
        
        private double conversion;
        Temperature(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Angle{
        degree(1), minute(60), seconds(3600), octant(0.022222222222222), sextant(0.01666666666666), 
        quadrant(0.011111111111111), revolution(0.0027777777777778), mil(17.777777777778), 
        radian(Mathx.Deg2Rad), grad(1.111111111111), circle(0.002777777777778); 
        
        private double conversion;
        Angle(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    /**
     * Convert value from one distance unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Distance from, Distance to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one area unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Area from, Area to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one volume unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Volume from, Volume to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one mass unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Mass from, Mass to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one speed unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Speed from, Speed to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one angular unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Angle from, Angle to){
        return value * (to.ConversionRate()) / from.ConversionRate();
    }
    
    /**
     * Convert value from one temperature unit to another
     * @param value
     * @param from
     * @param to
     * @return 
     */
    public static double Convert(double value, Temperature from, Temperature to){
        double cel = 0;
        switch(from){
            case kelvin:
                cel = K2C(value);
                break;
            case fahrenheit:
                cel = F2C(value);
                break;
            default:
                cel = value;
                break;
        }
        double res = 0;
        switch(to){
            case kelvin:
                res = C2K(cel);
                break;
            case fahrenheit:
                res = C2F(cel);
                break;
            default:
                res = cel;
                break;
        }
        
        return res;
    }
    
    private static double F2C(double f){
        return (f - 32) / 1.8;
    }
    private static double C2F(double c){
        return c * 1.8 + 32;
    }
    private static double C2K(double c){
        return c + 273.15;
    }
    private static double K2C(double k){
        return k - 273.15;
    }
    
}
