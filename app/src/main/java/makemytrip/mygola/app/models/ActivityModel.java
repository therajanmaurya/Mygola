package makemytrip.mygola.app.models;

import android.util.Log;

/**
 * Created by deves on 11/7/2015.
 */
public class ActivityModel {

	private String name;
	private String image;
	private int actual_price;
//	private int discount;
	private double rating;
	private String city;
	private String location;
	private String description;

	/**
	 *
	 * @return
	 * The name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 * The image
	 */
	public String getImage() {
		return image;
	}

	/**
	 *
	 * @param image
	 * The image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 *
	 * @return
	 * The actual_price
	 */
	public int getActual_price() {
		return actual_price;
	}

	/**
	 *
	 * @param actual_price
	 * The actual_price
	 */
	public void setActual_price(String actual_price) {
		this.actual_price = Integer.parseInt(actual_price);
	}

	/**
	 *
	 * @return
	 * The discount
	 */
//	public int getDiscount() {
//		return discount;
//	}

	/**
	 *
	 * @param discount
	 * The discount
	 */
//	public void setDiscount(String discount) {
//		Log.d("setDiscount", "Discount: " + discount);
//		discount = discount.substring(0, discount.length()-2);
//		Log.d("setDiscount", "Discount: " + discount);
//		this.discount = Integer.parseInt(discount);
//	}

	/**
	 *
	 * @return
	 * The rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 *
	 * @param rating
	 * The rating
	 */
	public void setRating(String rating) {
		this.rating = Double.parseDouble(rating);
	}

	/**
	 *
	 * @return
	 * The city
	 */
	public String getCity() {
		return city;
	}

	/**
	 *
	 * @param city
	 * The city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 *
	 * @return
	 * The location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 *
	 * @param location
	 * The location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 *
	 * @return
	 * The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @param description
	 * The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
