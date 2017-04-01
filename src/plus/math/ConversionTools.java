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
        Nanometre(1000000000), Femtometre(1E+15), Picometre(1E+12), Micrometre(1000000), Millimetre(1000), Centimetre(100), Decimetre(10), Metre(1), Dekametre(0.1), Kilometre(0.001), Megametre(0.000001), Gigametre(0.000000001), 
        League(0.0002071), Mile(0.0006214), Yard(1.094), Foot(3.281), Inch(39.37),
        Fathom(0.5468), LightYear(1.057008707E-16);
        
        private double conversion;
        Distance(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Area{
        SquareMillimetre(1000000), SquareCentimetre(10000), SquareMetre(1), SquareKilometre(0.000001),
        SquareMile(3.861021585425E-7), SquareYard(1.195990046301),
        Acre(0.0002471053814672), Hectare(0.0001); 
        
        private double conversion;
        Area(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Volume{
        CubicMillimetre(1000000000), CubicCentimetre(1000000), CubicDecimetre(1000), CubicMetre(1), CubicKilometre(0.000000001), 
        Millilitre(1000000), Centilire(100000), Litre(1000),
        Gallon(264.2), Quart(1057), Pint(2113), Gill(8454), Ounce(33810),
        Cup(4167), Tablespoon(66670), Teaspoon(200000),
        CubicMile(0.0000000002399), CubicYard(1.308), CubicFoot(35.31), CubicInch(61020); 
        
        private double conversion;
        Volume(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Mass{
        AtomicMassUnit(6.022e+26), Milligram(1000000), Gram(1000), Kilogram(1), Tonne(0.001), Kilotonne(0.000001),
        Ounce(35.27), Pound(2.205); 
        
        private double conversion;
        Mass(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Speed{
        MetresPerSecond(1), KilometresPerHour(3.6), KilometresPerSecond(0.001), 
        MetresPerMinute(60), MilesPerHour(2.237), FeetPerSecond(3.281), FeetPerMinute(196.9),
        Knot(1.944), C(0.000000003336); 
        
        private double conversion;
        Speed(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Temperature{
        Celsius(1), Kelvin(274.1), Fahrenheit(35.6); 
        
        private double conversion;
        Temperature(double ratio){
            this.conversion = ratio;
        }
        public double ConversionRate(){
            return conversion;
        }
    }
    
    public static enum Angle{
        Degree(1),Radian(Mathx.Deg2Rad), Milliradians(17.777777777778), Minute(60), Second(3600), Octant(0.022222222222222), Sextant(0.01666666666666), 
        Quadrant(0.011111111111111), Revolution(0.0027777777777778),
        Gradian(1.111111111111), Circle(0.002777777777778); 
        
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
            case Kelvin:
                cel = K2C(value);
                break;
            case Fahrenheit:
                cel = F2C(value);
                break;
            default:
                cel = value;
                break;
        }
        double res = 0;
        switch(to){
            case Kelvin:
                res = C2K(cel);
                break;
            case Fahrenheit:
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
