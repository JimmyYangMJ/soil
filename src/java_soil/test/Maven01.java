package test;

import org.apache.commons.math3.util. ArithmeticUtils;

public class Maven01 {

    public static void main(String[] args) {
        //计算两个整数的公约数
        int a = ArithmeticUtils.gcd(361, 285); //求最大公约数
        System.out.println("公约数" + a);
    }
}
