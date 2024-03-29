package org.example;

import org.nfunk.jep.JEP;
import org.nfunk.jep.function.NaturalLogarithm;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        double tolerance = 0.00001;
        int maxIterations = 10000;
        JEP jep = new JEP();
        jep.addStandardFunctions();
        jep.addStandardConstants();
        jep.addFunction("ln", new NaturalLogarithm());
        double x = 0;
        double x2 = 0;
        double result1 = 0;
        double result2 = 0;

        // Pedir al usuario que ingrese la expresión matemática
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa la expresión matemática (por ejemplo, x^2-2, e^(2*x)-3, ln(2*x): ");
        String expresion = scanner.nextLine();

        boolean band = false;

        do {
            try {

                band = true;
                System.out.print("Ingresa el valor de la frontera a: ");

                x = scanner.nextDouble();
                jep.addVariable("x", x);
                jep.parseExpression(expresion);
                result1 = jep.getValue();


                System.out.print("Ingresa el valor de la frontera b: ");

                x2 = scanner.nextDouble();
                jep.addVariable("x", x2);
                jep.parseExpression(expresion);
                result2 = jep.getValue();


                if (result1 * result2 > 0) {
                    System.out.println("Upss elige otro valor para las fronteras");
                    band = false;
                }
            }catch (Exception e){
                System.out.println("Upss ocurrio un error");
                band=false;
                scanner.next();
            }

        } while (!band);

        double root = falsaPosicion(expresion, jep, x, x2, tolerance, maxIterations);

         DecimalFormat formato = new DecimalFormat("#.#####");
        String resultado = formato.format(root);

        System.out.println("El resultado es: " + resultado);
    }

    public static double falsaPosicion(String expresion, JEP funcion, double a, double b, double tolerancia, int maxIterations) {
        int contador = 0;
        double c = 0;
        double result = 0;
        double result_a;
        double result_b;
        do {
            // Calcular el punto c utilizando la fórmula de la falsa posición

            funcion.addVariable("x", a);
            funcion.parseExpression(expresion);
            result_a = funcion.getValue();

            funcion.addVariable("x", b);
            funcion.parseExpression(expresion);
            result_b = funcion.getValue();


            c = ((a * result_b) - (b * result_a)) / (result_b - result_a);
            funcion.addVariable("x", c);
            funcion.parseExpression(expresion);
            result = funcion.getValue();


            if (Math.abs(result) == 0 || Math.abs(result) < tolerancia) {
                break;
            }
            if (result * result_a > 0) {
                a = c;
            } else {
                b = c;
            }
            contador++;

        } while (contador < maxIterations);

        return c;
    }
}