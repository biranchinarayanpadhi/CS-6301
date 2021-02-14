package Generics.LP1;

import java.util.*;
import java.math.*;

public class Num implements Comparable<Num> {

    static long defaultBase = 10; // Change as needed
    long base = defaultBase; // Change as needed
    long[] arr; // array to store arbitrarily large integers
    boolean isNegative = false; // boolean flag to represent negative numbers
    int len; // actual number of elements of array that are used; number is stored in
             // arr[0..len-1]

    private Num() {

    }

    public Num(String s) {

        int string_index = s.length() - 1;
        int array_index = 0;
        if (s.charAt(0) == '-') {
            this.isNegative = true;
        }
        if (this.isNegative) {
            arr = new long[s.length() - 1];
            for (; string_index > 0; string_index -= 1) {
                char ch = s.charAt(string_index);
                arr[array_index] = Character.getNumericValue(ch);
                array_index += 1;
            }
        } else {
            arr = new long[s.length()];
            for (; string_index >= 0; string_index -= 1) {
                char ch = s.charAt(string_index);
                arr[array_index] = Character.getNumericValue(ch);
                array_index += 1;
            }
        }
        this.len = array_index;
        // for(array_index=0;array_index<arr.length;array_index+=1){
        // System.out.print(arr[array_index]);
        // }

    }

    public Num(long x) {

        int arr_size = 0;
        if (x == 0) {
            arr_size = 1;
        }
        if (x < 0) {
            this.isNegative = true;
        }
        if (x != 0) {
            arr_size = (int) Math.floor(Math.log10(Math.abs(x))) + 1;
        }

        int array_index = 0;

        // System.out.println(arr_size+" "+x);
        arr = new long[arr_size];
        long temp = Math.abs(x);

        while (temp != 0) {
            long rem = temp % 10;
            arr[array_index] = rem;
            array_index += 1;
            temp = temp / 10;

        }
        this.len = arr_size;

    }

    public static Num add(Num a, Num b) {

        long[] arr_a = a.arr;
        long[] arr_b = b.arr;
        int index = 0;
        String ans = "";

        long carry = 0;
        int min_array_size = Math.min(arr_a.length, arr_b.length);

        if ((!a.isNegative && !b.isNegative) || (a.isNegative && b.isNegative)) {

            for (index = 0; index < min_array_size; index += 1) {
                long total = arr_a[index] + arr_b[index] + carry;
                long number_to_be_added = total % 10;

                if (total > 9) {
                    carry = total / 10;
                } else {
                    carry = 0;
                }

                ans += Long.toString(number_to_be_added);
            }
            if (index < arr_a.length) {
                for (; index < arr_a.length; index += 1) {

                    long total = arr_a[index] + carry;
                    long number_to_be_added = total % 10;

                    if (total > 9) {
                        carry = total / 10;
                    } else {
                        carry = 0;
                    }

                    ans += Long.toString(number_to_be_added);
                }
            }

            else if (index < arr_b.length) {
                for (; index < arr_b.length; index += 1) {

                    long total = arr_b[index] + carry;
                    long number_to_be_added = total % 10;

                    if (total > 9) {
                        carry = total / 10;
                    } else {
                        carry = 0;
                    }

                    ans += Long.toString(number_to_be_added);
                }
            }

            if (carry == 1) {
                ans += Long.toString(carry);
            }

        } else {

            Num output = new Num();
            if (a.compareTo(b) < 0) {
                output = output.substractLogic(b, a); // find the difference
                output.isNegative = b.isNegative; // give bigger number sign
            } else {
                output = output.substractLogic(a, b); // find the difference
                output.isNegative = a.isNegative; // give bigger number sign
            }
            return output;
        }

        if (a.isNegative && b.isNegative) {
            ans += "-";
        }

        Num output = new Num();
        StringBuffer str = new StringBuffer(ans);
        String final_ans = new String(str.reverse());
        String afterleadingzero = Num.trimZero(final_ans);
        if (afterleadingzero.equals("0")) {

            output.isNegative = false;
            output = new Num(afterleadingzero);
        }

        output = new Num(afterleadingzero);
        return output;
    }

    public static Num subtract(Num a, Num b) {

        Num output = new Num();

        if (!a.isNegative && b.isNegative) {
            b.isNegative = false;
            output = Num.add(a, b);
            b.isNegative = true;
            output.isNegative = false;
            return output;

        } else if (a.isNegative && !b.isNegative) {

            a.isNegative = false;
            output = Num.add(a, b);
            a.isNegative = true;
            output.isNegative = true;
            return output;

        } else {

            if (a.compareTo(b) < 0) {
                output = output.substractLogic(b, a); // find the difference
                output.isNegative = !b.isNegative; // give bigger number sign* -(-b) is +ve
            } else {
                output = output.substractLogic(a, b); // find the difference
                output.isNegative = a.isNegative; // give bigger number sign
            }

            return output;

        }
    }

    private Num substractLogic(Num a, Num b) {
        long[] arr_a = a.arr;
        long[] arr_b = b.arr;
        String ans = "";
        int arr_index = 0;

        long min_array_size = Math.min(arr_a.length, arr_b.length);
        for (; arr_index < min_array_size; arr_index += 1) {

            long number_to_be_added = 0;

            if (arr_a[arr_index] >= arr_b[arr_index]) {
                number_to_be_added = arr_a[arr_index] - arr_b[arr_index];
            } else {
                int temp = arr_index + 1;
                number_to_be_added = arr_a[arr_index] + 10 - arr_b[arr_index];
                while (temp < arr_a.length) {

                    if (arr_a[temp] != 0) {
                        arr_a[temp] -= 1;

                        break;
                    } else {

                        arr_a[temp] = 9;
                        temp += 1;
                    }
                }
            }
            ans += Long.toString(number_to_be_added);
        }

        if (arr_index < arr_a.length) {
            for (; arr_index < arr_a.length; arr_index += 1) {

                ans += Long.toString(arr_a[arr_index]);

            }
        } else {
            for (; arr_index < arr_b.length; arr_index += 1) {
                ans += Long.toString(arr_b[arr_index]);
            }

        }
        Num output = new Num();
        StringBuffer str = new StringBuffer(ans);
        String final_ans = new String(str.reverse());
        String afterleadingzero = Num.trimZero(final_ans);
        if (afterleadingzero.equals("0")) {

            output.isNegative = false;
            output = new Num(afterleadingzero);
        }

        output = new Num(afterleadingzero);
        return output;
    }

    private static String trimZero(String str) {
        int index = 0;
        while (index < str.length() && str.charAt(index) == '0') {
            index += 1;
        }
        if (index < str.length()) {
            return str.substring(index, str.length());
        }
        return "0";
    }

    public static Num product(Num a, Num b) {

        Num output = new Num();

        output = output.simpleProduct(a, b);

        if (a.isNegative == b.isNegative) {
            output.isNegative = false;
        } else {
            output.isNegative = true;
        }
        return output;
    }

    private Num simpleProduct(Num a, Num b) {

        Num output = new Num();
        long[] arr_a = a.arr;
        long[] arr_b = b.arr;
        String result = "";

        if ((a.len == 1 && a.arr[0] == 0) || (b.len == 1 && b.arr[0] == 0)) {

            return new Num(0);

        } else {

            int total_length = a.len + b.len;
            long[] output_arr = new long[total_length];
            int index = 0;
            int output_pointer = 1;

            int i = 0;
            int j = 0;
            int carry = 0;

            while (i < arr_a.length) {

                while (j < arr_b.length) {

                    long product = (arr_b[j] * arr_a[i]) + carry;
                    output_arr[index] = output_arr[index] + product;
                    index += 1;
                    j += 1;

                }
                index = output_pointer;
                output_pointer += 1;
                i += 1;
                j = 0;
            }
            carry = 0;

            index = 0;
            while (index < total_length) {
                long number_to_be_added = (output_arr[index] + carry) % 10;
                carry = ((int) output_arr[index] + carry) / 10;
                output_arr[index] = number_to_be_added;
                result += Long.toString(number_to_be_added);
                index += 1;
            }
            StringBuffer ans = new StringBuffer(result);
            String final_ans = new String(ans.reverse());
            String afterleadingzero = Num.trimZero(final_ans);
            output = new Num(afterleadingzero);
            return output;

        }

    }

    // Use divide and conquer
    public static Num power(Num a, long n) {

        Num output = new Num();
        Num one = new Num(1);
        Num zero = new Num(0);
        Num minusOne = new Num(-1);

        // Assuming n is always positive as given in the question
        if (n == 0) {
            if (a.compareTo(zero) == 0) {
                throw new ArithmeticException("power(0,0) is Undefined!");
            } else {
                return one;
            }
        } else {

            if (a.compareTo(zero) == 0)
                return zero; // power(0,positive)

            if (a.compareTo(one) == 0 && a.isNegative) { // a = -1
                if (n % 2 == 0)
                    return one; // power(-1, positiveEven)
                else
                    return minusOne; // power(-1,positiveOdd)
            }

            if (a.compareTo(one) == 0)
                return one; // power(1,positive)

            // When a is Negative
            if (a.compareTo(zero) > 0 && a.isNegative) {
                if (n % 2 == 1)
                    output.isNegative = true; // power(negative, positiveEven)
                output = output.findPower(a, n);
                return output;
            }

            // When a is Positive
            else
                return output.findPower(a, n); // power(positive, positive)
        }

    }

    private Num findPower(Num a, long n) {

        if (n == 0) {
            return new Num(1);
        }
        if (n == 1) {
            return a;
        }

        Num halfPower = findPower(a, n / 2);

        if (n % 2 == 0) {
            return product(halfPower, halfPower);
        } else {
            return product(a, product(halfPower, halfPower));
        }
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
        Num output = new Num();
        Num zero = new Num(0);
        Num one = new Num(1);
        Num minusOne = new Num(-1);
        Num two = new Num(2);

        Num mid = new Num();

        // numerator is zero
        if (a.compareTo(zero) == 0) {
            return zero;
        }

        // divide by zero
        if (b.compareTo(zero) == 0) {

            return null;
        }

        // denominator is 1/-1
        if (b.compareTo(one) == 0) {

            output = a;
            if (a.isNegative != b.isNegative) {
                output.isNegative = true;
            } else {
                output.isNegative = false;
            }
            return output;

        }

        // division by 2
        if (b.compareTo(two) == 0) {
            output = a.by2();
            if (a.isNegative != b.isNegative) {
                output.isNegative = true;
            } else {
                output.isNegative = false;
            }
            return output;
        }

        // |a| < |b|, causing division either ZERO or minusOne
        if (a.compareTo(b) < 0) {
            if (a.isNegative == b.isNegative) {
                zero.isNegative = false;
            }
            return zero;

        }

        // |a| == |b|
        if (a.compareTo(b) == 0) {
            if (a.isNegative == b.isNegative)
                return one;
            else
                return minusOne;
        }

        Num low = new Num(1);
        Num high = a;

        while (low.compareTo(high) < 0) {

            Num sum = Num.add(low, high);
            mid = sum.by2();

            if (mid.compareTo(low) == 0) {
                break;
            }

            Num product = Num.product(mid, b);

            if (product.compareTo(a) == 0) {

                break;

            } else if (product.compareTo(a) > 0) {

                high = mid;

            } else {

                low = mid;

            }

        }
        // When a and b has different signs
        if (a.isNegative != b.isNegative) {
            mid.isNegative = true;
        }
        else{
            mid.isNegative=false;
        }

        return mid;

    }

    // return a%b
    public static Num mod(Num a, Num b) {

        Num one = new Num(1);
        Num zero  = new Num(0);

        //assuming a >0 and b>0 and returning null when b is 0
        if (b.compareTo(zero) == 0){
            return null;
        }

        //assuming B should not be -1
        if(b.compareTo(one) == 0){
            return a;
        }

        Num quotient = Num.divide(a, b);
        Num product = Num.product(quotient, b);

        Num remainder = Num.subtract(a, product);

        return remainder;

    }

    // Use binary search
    public static Num squareRoot(Num a) {


        Num one = new Num(1);
        Num zero  = new Num(0);

        //if a is negative
        if(a.isNegative){
            return null;
        }

        //if a is zero
        if(a.compareTo(zero) == 0){
            return zero;
        }

        //if a is one
        if(a.compareTo(one) == 0){
            return one;
        }

        //binary search starts
        Num high=a;
        Num low = new Num(0);
        Num sum = new Num();
        Num mid = new Num();
        Num product= new Num();

        //very similar to divide except the fact that we are checking if mid*mid = a
        while (low.compareTo(high)<0){

            sum=Num.add(low, high);
            mid = sum.by2();

            if (low.compareTo(mid) == 0){
                break;
            }

            product = Num.product(mid, mid);
            if(product.compareTo(a) == 0){
                return mid;
            }

            else if(product.compareTo(a)<0){
                low=mid;
            }
            else{
                high=mid;
            }
        }
        return mid;
    }

    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1
    // otherwise
    public int compareTo(Num other) {

        if (other.len > this.len) {
            return -1;
        } else if (other.len < this.len) {
            return 1;
        } else {

            int index = this.len - 1;
            while (index > -1) {
                // Inequality check: this < other
                if (this.arr[index] < other.arr[index])
                    return -1;

                // Inequality check: other < this
                if (other.arr[index] < this.arr[index])
                    return 1;

                index--;
            }

        }
        return 0;
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {

    }

    // Return number to a string in base 10
    public String toString() {

        String s = "";
        int index = this.arr.length - 1;
        for (; index >= 0; index -= 1) {
            s += this.arr[index];
        }

        return s;
    }

    public long base() {
        return this.base;
    }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
        return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {

        Num two = new Num(2);
        Num zero = new Num(0);
        Num output = new Num();
        String ans = "";

        // if numerator is zero
        if (this.compareTo(two) < 0) {
            return zero;
        }

        int carry = 0;
        int arr_index = this.arr.length - 1;

        while (arr_index >= 0) {
            long number_to_be_added = (carry * 10 + this.arr[arr_index]) / 2;
            if (this.arr[arr_index] % 2 == 0) {
                carry = 0;
            } else {
                carry = 1;
            }
            ans += number_to_be_added;
            arr_index -= 1;
        }
        String final_ans = Num.trimZero(ans);

        output = new Num(final_ans);
        if (this.isNegative) {
            output.isNegative = true;
        }
        return output;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*. There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        return null;
    }

    // Parse/evaluate an expression in infix and return resulting number
    // Input expression is a string, e.g., "(3 + 4) * 5"
    // Tokenize the string and then input them to parser
    // Implementing this method correctly earns you an excellence credit
    public static Num evaluateExp(String expr) {
        return null;
    }

    public static void main(String[] args) {
        Num x = new Num(4854665465L);
        Num y = new Num("-14");
        Num z = Num.squareRoot(y);
        System.out.println(z + " " + z.isNegative);
        // System.out.println(y.compareTo(x));
        // Num a = Num.power(x, 8);
        // System.out.println(a);
        if (z != null)
            z.printList();
    }
}