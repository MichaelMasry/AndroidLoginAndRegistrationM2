package info.androidhive.loginandregistration.helper;

public class Review {

    private String reviewText;
    private String owner;

    public Review(String reviewText,String owner){
        this.reviewText=reviewText;
        this.owner=owner;
    }

    public void setReviewText(String r){ this.reviewText=r;}
    public void setOwner(String r){ this.owner=r;}
    public String getReviewText(){return this.reviewText;}
    public String getOwner(){return this.owner;}

}
