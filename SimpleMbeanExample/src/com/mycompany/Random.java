package com.mycompany;

public class Random implements RandomMBean {

	@Override
	public int generateRandom() {
		// TODO Auto-generated method stub

		int range = 10;

		int randomNumber = (int) (Math.random() * range);

		return randomNumber;
	}

}
