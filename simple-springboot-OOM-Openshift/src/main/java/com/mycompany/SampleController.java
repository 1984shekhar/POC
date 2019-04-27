package com.mycompany;

import java.io.PrintStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SampleController
{
  @RequestMapping({"/"})
  @ResponseBody
  String home()
    throws Exception
  {
    new SampleController().getOOMHeap();
    return "Hello World!";
  }
  
  private void getOOMHeap()
    throws Exception
  {
    int i = 100;
    System.out.println("\n=================> OOM test started..\n");
    for (int j = 1; j < 20; j++)
    {
      System.out.println("Iteration " + j + " Free Mem: " + Runtime.getRuntime().freeMemory());
      System.out.println("Iteration " + j + " Max Mem: " + Runtime.getRuntime().maxMemory());
      int[] k = new int[i];
      i *= 10;
      System.out.println("\nRequired Memory for next loop: " + i);
      Thread.sleep(1000L);
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    SpringApplication.run(SampleController.class, args);
  }
}
