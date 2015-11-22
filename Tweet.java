import java.util.ArrayList;

public class Tweet {
	long tweetID;
	int clusterID;
	String tweetString;
	ArrayList<String> words = new ArrayList<String>();
	double jaccardDistance;
	public static ArrayList<String> symbols = new ArrayList<String>();
	
	void setSymbols(){
		symbols.add("RT");
		symbols.add("-");
		symbols.add("|");
		symbols.add("\n");
		symbols.add(" ");
		symbols.add("");
		symbols.add("@");
	}
	
	Tweet(){
		this.tweetID = 0;
		this.clusterID = -5;
		this.tweetString = "OOPS";
		this.jaccardDistance = 0.0;
	}
	
	Tweet(long tID, String tString){
		this.tweetID = tID;
		this.tweetString = tString;
		this.clusterID = -4;
		this.jaccardDistance = 0.0;
	}
	
	void setClusterID(int clustID){
		this.clusterID = clustID;
	}
	
	void setJaccardDist(double dist){
		this.jaccardDistance = dist;
	}
	
	void setTweetID(long tweetID){
		this.tweetID = tweetID;
	}
	
	void setTweetString(String tweet){
		tweet = tweet.replaceAll("@\\p{L}+", "");
		this.tweetString = tweet.replaceAll("[!.+,:()\"\']", "");		
		setTweetWords(this.tweetString);		
	}
	
	void setTweetWords(String tweet){		
		String[] lines = tweet.split("\n");
		for(String line : lines){
			String[] arr = line.split(" ");
			for( String word : arr){	
			//	word = removeSymbols(word);
				this.words.add(word);
			}			
		}
		removeRTWords(words);
	}
	
	void removeRTWords(ArrayList<String> words){
		setSymbols();
		words.removeAll(symbols);
	}
	
	void setTweet(long tweetID, String tweet){
		setTweetID(tweetID);
		setTweetString(tweet);
	}
	
	void printTweet(){
		System.out.println("------------------------------------------------------------------------");
		System.out.println("User/Tweet ID  :"+this.tweetID);
		System.out.println("Cluster ID     :"+this.clusterID);
		System.out.println("Tweet String   :"+this.tweetString);
		System.out.println("Jaccardian Dist:"+this.jaccardDistance);
		printTweetWords();
		printTweetWordsSize();
	}	
	
	//delete this after testing
	void printTweetWordsSize(){
		System.out.println(this.words.size());
	}
	
	//delete this after testing	
	void printTweetWords(){
		System.out.println(this.words);
	}
	
	double getCommonWordCount(Tweet t1){
		ArrayList<String> tweetStringOne = t1.words;
		ArrayList<String> realCopyOfTweetStringTwo = this.words;		
		ArrayList<String> tweetStringTwo = (ArrayList<String>) realCopyOfTweetStringTwo.clone();
		int commonWordCount = 0;
		for(int i = 0; i < tweetStringOne.size() ; i++){
			for(int j = 0; j < tweetStringTwo.size() ; j++){
				 if(tweetStringOne.get(i).equals(tweetStringTwo.get(j))){
					 tweetStringTwo.remove(j);
					 commonWordCount++;
					 break;
				 }
			}
		}
		return commonWordCount;
	}
	
	double getTotalWordCount(Tweet t1){
		return (t1.words.size() + this.words.size());
	}
	
	double getJaccardianDistance(Tweet centeroidTweet){
		return( 1 -( this.getCommonWordCount(centeroidTweet) / this.getTotalWordCount(centeroidTweet)));
	}
	
	long getTweetID(){
		return this.tweetID;
	}
	
	ArrayList<String> getWords(){
		return this.words;
	}
	
	int getClusterID(){
		return this.clusterID;
	}
}

