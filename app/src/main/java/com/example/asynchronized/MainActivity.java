package com.example.asynchronized;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//A. 无论synchronized关键字加在方法上还是对象上，如果它作用的对象是非静态的，则它取得的锁是对象；
// 如果synchronized作用的对象是一个静态方法或一个类，则它取得的锁是对类，该类所有的对象同一把锁。
// B. 每个对象只有一个锁（lock）与之相关联，谁拿到这个锁谁就可以运行它所控制的那段代码。
// C. 实现同步是要很大的系统开销作为代价的，甚至可能造成死锁，所以尽量避免无谓的同步控制。

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
        //synchronized作用于一个类T时，是给这个类T加锁，T的所有对象用的是同一把锁。
    }

    /**
     * 修饰一个静态的方法
     */
    private void test5() {
//        syncThread1和syncThread2是SyncThread1的两个对象，但在thread1和thread2并发执行时却保持了线程同步。这是因为run中调用了静态方法method，
//        而静态方法是属于类的，所以syncThread1和syncThread2相当于用了同一把锁。这与test1是不同的。
        SyncThread1 syncThread1 = new SyncThread1();
        SyncThread1 syncThread2 = new SyncThread1();
        Thread thread1 = new Thread(syncThread1, "SyncThread1");
        Thread thread2 = new Thread(syncThread2, "SyncThread2");
        thread1.start();
        thread2.start();
    }

    /**
     * 指定要给某个对象加锁
     */
    private void test4() {
//        在AccountOperator 类中的run方法里，我们用synchronized 给account对象加了锁。这时，当一个线程访问account对象时，其他试图访问account对象的线程将会阻塞，
// 直到该线程访问account对象结束。也就是说谁拿到那个锁谁就可以运行它所控制的那段代码。
//        当有一个明确的对象作为锁时，就可以用类似下面这样的方式写程序。
        Account account = new Account("zhang san", 10000.0f);
        AccountOperator accountOperator = new AccountOperator(account);

        final int THREAD_NUM = 5;
        Thread threads[] = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i ++) {
            threads[i] = new Thread(accountOperator, "Thread" + i);
            threads[i].start();
        }
    }

    private void test3() {
//        当一个线程访问对象的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块
        Counter counter = new Counter();
        Thread thread1 = new Thread(counter, "A");
        Thread thread2 = new Thread(counter, "B");
        thread1.start();
        thread2.start();
    }

    private void test2() {
//        创建了两个SyncThread的对象syncThread1和syncThread2，线程thread1执行的是syncThread1对象中的synchronized代码(run)，
// 而线程thread2执行的是syncThread2对象中的synchronized代码(run)；我们知道synchronized锁定的是对象，
// 这时会有两把锁分别锁定syncThread1对象和syncThread2对象，而这两把锁是互不干扰的，不形成互斥，所以两个线程可以同时执行。
        SyncThread syncThread1 = new SyncThread();
        SyncThread syncThread2 = new SyncThread();
        Thread thread1 = new Thread(syncThread1, "SyncThread1");
        Thread thread2 = new Thread(syncThread2, "SyncThread2");
        thread1.start();
        thread2.start();
    }

    /**
     * 修饰一个代码块
     */
    private void test1() {
//        当两个并发线程(thread1和thread2)访问同一个对象(syncThread)中的synchronized代码块时，在同一时刻只能有一个线程得到执行，
// 另一个线程受阻塞，必须等待当前线程执行完这个代码块以后才能执行该代码块。Thread1和thread2是互斥的，因为在执行synchronized代码块时会锁定当前的对象，
// 只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象。
        SyncThread syncThread = new SyncThread();
        Thread thread1 = new Thread(syncThread, "SyncThread1");
        Thread thread2 = new Thread(syncThread, "SyncThread2");
        thread1.start();
        thread2.start();
    }
    /**
     * 同步线程
     */
    static class SyncThread implements Runnable {
        private static int count;

        public SyncThread() {
            count = 0;
        }
//        写法一修饰的是一个方法，写法二修饰的是一个代码块，但写法一与写法二是等价的，都是锁定了整个方法时的内容。
//        1. synchronized关键字不能继承。在定义接口方法时不能使用synchronized关键字。
//        2.构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。

//        public  void run() {
//            synchronized(this) {
//                for (int i = 0; i < 5; i++) {
//                    try {
//                        System.out.println(Thread.currentThread().getName() + ":" + (count++));
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        synchronized public  void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ":" + (count++));
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }

        public int getCount() {
            return count;
        }
    }

    class Counter implements Runnable{
        private int count;

        public Counter() {
            count = 0;
        }

        public void countAdd() {
            synchronized(this) {
                for (int i = 0; i < 5; i ++) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ":" + (count++));
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //非synchronized代码块，未对count进行读写操作，所以可以不用synchronized
        public void printCount() {
            for (int i = 0; i < 5; i ++) {
                try {
                    System.out.println(Thread.currentThread().getName() + " count:" + count);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run() {
            String threadName = Thread.currentThread().getName();
            if (threadName.equals("A")) {
                countAdd();
            } else if (threadName.equals("B")) {
                printCount();
            }
        }
    }

    /**
     * 银行账户类
     */
    class Account {
        String name;
        float amount;

        public Account(String name, float amount) {
            this.name = name;
            this.amount = amount;
        }
        //存钱
        public  void deposit(float amt) {
            amount += amt;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //取钱
        public  void withdraw(float amt) {
            amount -= amt;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public float getBalance() {
            return amount;
        }
    }

    /**
     * 账户操作类
     */
    class AccountOperator implements Runnable{
        private Account account;
        public AccountOperator(Account account) {
            this.account = account;
        }

        public void run() {
            synchronized (account) {
                account.deposit(500);
                account.withdraw(400);
                System.out.println(Thread.currentThread().getName() + ":" + account.getBalance());
            }
        }
    }

    static class SyncThread1 implements Runnable {
        private static int count;

        public SyncThread1() {
            count = 0;
        }

        public synchronized static void method() {
            for (int i = 0; i < 5; i ++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ":" + (count++));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void run() {
            method();
        }
    }

}
