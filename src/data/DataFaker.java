package data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.Timestamp;

import com.github.javafaker.Faker;

import application.MD5Hashing;

public class DataFaker {
	
	private List<Float> foodPrices = new ArrayList<Float>();
	private List<Float> billPrices = new ArrayList<Float>();
	
	private int OTHER_TABLE_COUNT = 200000;
	private int MAIN_TABLE_COUNT = 1200000;

	public void initFaker()
	{
		foodPrices.add(0f); //Aby nieco bolo na 0 mieste
		for(int i=0;i<=OTHER_TABLE_COUNT;i++)
		{
			billPrices.add(0f);
		}
		
		createIngredients();
		createChefs();
		//createUsers();
		createOrders();
		createFood();
		createFoodOrders();
		createBills();
		createFoodChef();
		createFoodIngredients();
	}
	
	private void createIngredients()
	{
		Faker faker = new Faker();		
		StringBuilder sb = new StringBuilder();
		sb.append("ID,NAME\n");
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			
			sb.append(String.valueOf(i)+","+faker.food().ingredient()+String.valueOf(i)+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("ingredients.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ING DONE");
	}
	
	private void createChefs()
	{
		Faker faker = new Faker();		
		StringBuilder sb = new StringBuilder();
		sb.append("ID,NAME\n");
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			
			sb.append(String.valueOf(i)+","+faker.name()+String.valueOf(i)+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("chefs.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("CHEFS DONE");
	}
	
	private void createUsers()
	{
		Faker faker = new Faker();		
		StringBuilder sb = new StringBuilder();
		sb.append("ID,NAME,ADDRESS, PASSWORD,EMAIL,PRIVILEDGED\n");
		final Random random = new Random();
		
		try {
			PrintWriter writer = new PrintWriter(new File("users.csv"));
			
			for(int i=1;i<=MAIN_TABLE_COUNT;i++)
			{
				sb.append(String.valueOf(i)+","+faker.name().fullName()+","+faker.address().fullAddress().replaceAll(",", " ")+","
						+MD5Hashing.getSecurePassword(faker.leagueOfLegends().champion()+String.valueOf(random.nextInt()))
						+","+String.valueOf(i)+faker.internet().safeEmailAddress()+","+String.valueOf(0)+"\n");
				
				if(i%100000 == 0)
				{
					System.out.println("USERS "+i+" DONE");
					writer.write(sb.toString());
					sb=new StringBuilder();
				}

			}
			
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("USERS DONE");
	}
	
	private void createOrders()
	{
		Faker faker = new Faker();		
		StringBuilder sb = new StringBuilder();
		sb.append("ID,TIME,USER_ID\n");
		final Random random = new Random();
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			/*int user_id = (int)(random.nextLong()%1200000);
			if(user_id < 0)	
				user_id*=-1;
			else 
				if(user_id == 0)
					user_id++;*/
			
			int user_id = random.nextInt(OTHER_TABLE_COUNT)+1;
			Timestamp timestamp = new Timestamp(faker.date().birthday().getTime());
			sb.append(String.valueOf(i)+","+timestamp+","+String.valueOf(user_id)+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("orders.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ORDERS DONE");
	}
	
	private void createFood()
	{
		Faker faker = new Faker();		
		StringBuilder sb = new StringBuilder();
		sb.append("ID,NAME,PRICE\n");
		final Random random = new Random();
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			/*int randomInt = (random.nextInt()%1000);
			if(randomInt < 0) 
				randomInt*=-1;
			else
				if(randomInt == 0) 
					randomInt++;*/
			float price = random.nextInt(1000)/100.0f;
			
			foodPrices.add(price);
			sb.append(String.valueOf(i)+","+faker.food().dish()+String.valueOf(i)+","+String.valueOf(price)+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("food.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FOOD DONE");
	}
	
	private void createFoodOrders()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ID,FOOD_ID,ORDER_ID,COUNT\n");
		final Random random= new Random();
		int id = 1;
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			/*int max_iter = random.nextInt()%3;
			if(max_iter <0) 
				max_iter*=-1;
			else 
				if(max_iter == 0) 
					max_iter++;*/
			
			int max_iter = random.nextInt(3)+1;
			
			for(int j=0;j<max_iter+1;j++){
				/*int food_id = (int)(random.nextLong() % OTHER_TABLE_COUNT);
				if(food_id < 0)	
					food_id*=-1;
				else 
					if (food_id==0) 
						food_id++;
				
				int count = (random.nextInt() % 3);
				if(count < 0) 
					count*=-1;
				else 
					if (count ==0)	
						count++;
				*/
				int food_id = random.nextInt(OTHER_TABLE_COUNT)+1;
				int count = random.nextInt(3)+1;
				
				sb.append(String.valueOf(id)+","+String.valueOf(food_id)+","+String.valueOf(i)+","+String.valueOf(count)+"\n");
				billPrices.set(i, billPrices.get(i)+count*foodPrices.get(food_id));
				
				id++;
			}
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("food_orders.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FOOD ORDERS DONE");
	}
	
	private void createBills()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ID,ORDER_ID,PAID,PRICE\n");
		final Random random = new Random();
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			int paid;
			if(random.nextBoolean())
				paid = 1;
			else
				paid = 0;
			
			sb.append(String.valueOf(i)+","+String.valueOf(i)+","+String.valueOf(paid)+","+String.valueOf(billPrices.get(i))+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("bills.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("BILLS DONE");
	}
	
	private void createFoodChef()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ID,CHEF_ID,FOOD_ID\n");
		final Random random = new Random();
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			int chef_id = random.nextInt(OTHER_TABLE_COUNT)+1;
			sb.append(String.valueOf(i)+","+String.valueOf(chef_id)+","+String.valueOf(i)+"\n");
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("food_chef.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FOOD CHEF DONE");
	}
	
	private void createFoodIngredients()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ID,FOOD_ID,INGREDIENTS_ID\n");
		final Random random = new Random();
		int id = 1;
		
		for(int i=1;i<=OTHER_TABLE_COUNT;i++)
		{
			for(int j=0; j<random.nextInt(4)+1;j++) {				
				int ing_id = random.nextInt(OTHER_TABLE_COUNT)+1;
				sb.append(String.valueOf(id)+","+String.valueOf(i)+","+String.valueOf(ing_id)+"\n");
				id++;
			}
		}
		
		//Zapis do csv suboru
		try {
			PrintWriter writer = new PrintWriter(new File("food_ingredients.csv"));
			writer.write(sb.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FOOD INGREDIENTS DONE");
	}
}
