import java.util.Scanner;

public class AssignmentTwo {
	interface Shape {
		public double calculateArea();
		public void display();
	}
	static class Rectangle implements Shape {
		private double length;
		private double width;
		public Rectangle(double length, double width) {
			this.length = length;
			this.width = width;
		}
		public double calculateArea() {
			return length * width;
		}
		public void display() {
			System.out.println("\nRectangle Specs");
			System.out.println("Length: " + length);
			System.out.println("Width: " + width);
			System.out.println("Area: " + calculateArea());
		}
	}
	static class Circle implements Shape {
		private double radius;
		public Circle(double radius) {
			this.radius = radius;
		}
		public double calculateArea() {
			return Math.PI * Math.pow(radius,  2);
		}
		public void display() {
			System.out.println("\nCircle Specs");
			System.out.println("Radius: " + radius);
			System.out.println("Area " + calculateArea());
		}
	}
	static class Triangle implements Shape {
		private double base;
		private double height;
		public Triangle(double base, double height) {
			this.base = base;
			this.height = height;
		}
		public double calculateArea() {
			return base * height / 2;
		}
		public void display() {
			System.out.println("\nTriangle Specs");
			System.out.println("Base: " + base);
			System.out.println("Height: " + height);
			System.out.println("Area: " + calculateArea());
		}
	}
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String[] input = null;
		long sum = 0;
		
		System.out.println("Enter numbers to add separated by spaces:");
		input = s.nextLine().split(" ");
		for (String i : input) {
			try {
				sum += Long.parseLong(i);
			} catch (Exception e) { }
		}
		System.out.println("Sum is: " + sum);
		
		Integer max=Integer.MIN_VALUE, x=-1, y=-1;
		int[][] a = {{1,2,3}, {4,5,6}, {7,8,9}};
		for (int i=0; i<a.length; i++) {
			for (int j=0; j<a[i].length; j++) {
				int num = a[i][j];
				if (num > max) {
					max = num;
					x = i;
					y = j;
				}
			}
		}
		System.out.println("Max: " + max + ", x: " + x + ", y: " + y);
		
		Rectangle r = new Rectangle(5, 5);
		r.display();
		Circle c = new Circle(5);
		c.display();
		Triangle t = new Triangle(5, 5);
		t.display();
	}
}