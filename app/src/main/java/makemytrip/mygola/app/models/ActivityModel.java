package makemytrip.mygola.app.models;

/**
 * Created by deves on 11/7/2015.
 */
public class ActivityModel {

	private int Id;

	public int getId()
	{
		return Id;
	}

	public void setId(int id)
	{
		Id = id;
	}

	private String name;
	private String image;
	private int actual_price;
	private String discount;
	private float rating;
	private String city;
	private String location;
	private String description;
	private boolean favorite = false;

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	public boolean isFavorite()
	{
		return favorite;
	}

	public void setFavorite(boolean favorite)
	{
		this.favorite = favorite;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
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
