//package net.thevpc.jeep;
//
//import java.util.Objects;
//
//public class JComplex64 {
//    private double real;
//    private double imag;
//
//    public JComplex64(double real, double imag) {
//        this.real = real;
//        this.imag = imag;
//    }
//
//    public double real() {
//        return real;
//    }
//
//    public double imag() {
//        return imag;
//    }
//
//    @Override
//    public String toString() {
//        if(real==0 && imag==0){
//            return "0.0";
//        }
//        if(imag==0){
//            return String.valueOf(real);
//        }
//        if(real==0){
//            return imag +"î";
//        }
//        if(imag>0){
//            return real+"+"+imag+"î";
//        }
//        return real+imag+"î";
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        JComplex64 that = (JComplex64) o;
//        return Double.compare(that.real, real) == 0 &&
//                Double.compare(that.imag, imag) == 0;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(real, imag);
//    }
//}
