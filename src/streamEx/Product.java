package streamEx;

public class Product {
	private int amount;
	private String name;
	public Product(int amount, String name) {
		super();
		this.amount = amount;
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Product [amount=" + amount + ", name=" + name + "]";
	}
	
	
	
}
