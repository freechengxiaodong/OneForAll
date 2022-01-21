package com.payment.exception;

public class TestException {


    public TestException() {

    }


    boolean testEx() throws Exception {
        boolean ret = true;
        try {
            ret = testEx1();

        } catch (Exception e) {
            System.out.println("testEx, catch exception");
            ret = false;
            throw e;

        } finally {

            System.out.println("testEx, finally; return value=" + ret);
            return ret;

        }

        //return ret;
    }


    boolean testEx1() throws Exception {
        boolean ret = true;
        try {
            ret = testEx2();
            if (!ret) {
                return false;
            }
            System.out.println("testEx1, at the end of try");

        } catch (Exception e) {
            System.out.println("testEx1, catch exception");
            ret = false;
            throw e;

        } finally {
            System.out.println("testEx1, finally; return value=" + ret);
            return ret;

        }

        //return ret;
    }


    boolean testEx2() throws Exception {
        boolean ret = true;
        try {
            int b = 12;
            int c;
            for (int i = 2; i >= -2; i--) {
                c = b / i;
                System.out.println("i=" + i);

            }

            ret = true;

        } catch (Exception e) {
            System.out.println("testEx2, catch exception");
            ret = false;
            throw e;

        } finally {
            System.out.println("testEx2, finally; return value=" + ret);
            return ret;     //  finally语句块不应该出现return，如果有return，程序将在finally执行完直接return终止。catch块中有throw异常，如果finally里有return，也会导致程序终止，不会抛出异常。

        }

        //return true;
    }


    public static void main(String[] args) {
        TestException testException1 = new TestException();
        try {
            testException1.testEx();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}