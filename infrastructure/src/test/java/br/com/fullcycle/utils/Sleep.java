package br.com.fullcycle.utils;

import java.util.concurrent.TimeUnit;

public class Sleep {
  public static void sleep(Integer seconds) {
    try {
            TimeUnit.SECONDS.sleep(seconds);
      } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("The thread was interrupted.");
      }
  }
}
