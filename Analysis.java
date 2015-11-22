import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.json.*;

public class Analysis {
	
	//Initial set of values to be used!
	public static String inputFileName;
	public static String outputFileName;
	public static String centeroidTweetIDFileName;
	public static ArrayList<Long> centeroidTweetID = new ArrayList<Long>();
	public static ArrayList<Tweet> centeriodTweetList = new ArrayList<Tweet>();
	public static ArrayList<Tweet> overallTweetList = new ArrayList<Tweet>();
	
	//Initialize the centeroid tweets using the InitialSeed.txt file!
	public static void setCenteroidTweetsList(int numOfClusters, String CENTEROID_ID_FILE_NAME) throws IOException{
		BufferedReader buffer = new BufferedReader(new FileReader(CENTEROID_ID_FILE_NAME));
		String tweetID = null;
		while((tweetID = buffer.readLine()) != null){
			String [] valueFromTxt = tweetID.split(",");
			//System.out.println(valueFromTxt[0]);
			centeroidTweetID.add(Long.parseLong(valueFromTxt[0]));			
		}
		for(int i = 0; i<centeroidTweetID.size();i++){			
			for(int j = 0; j<overallTweetList.size(); j++){
				if(overallTweetList.get(j).getTweetID() == centeroidTweetID.get(i) ){
					centeriodTweetList.add(overallTweetList.get(j));
					centeriodTweetList.get(i).setClusterID(i+1);
					break;
				}
			}
		}
		buffer.close();
	}
	
	//Parse the JSON file and store the values of the tweets in ArrayList!
	public static void setInputTweetList(String INPUT_FILE) throws IOException{
		BufferedReader bfReader = new BufferedReader(new FileReader(INPUT_FILE));
		String line;
		JsonReader reader;
		JsonObject tweetObj;
		String tempTweetString;
		long tweetID;
		while((line = bfReader.readLine()) != null){			 
			Tweet t1 = new Tweet();
			reader = Json.createReader(new StringReader(line));
			tweetObj = reader.readObject();
			tweetID = Long.parseLong(tweetObj.get("id").toString());
			tempTweetString = tweetObj.get("text").toString();
			overallTweetList.add(t1);
			t1.setTweet(tweetID, tempTweetString);
			//System.out.println(tweetID);
		}
		bfReader.close();
	}
	
	//Initialize the cluster for the first iteration!
	public static void initializeFirtIteration(int K, String CENT_FILE_PATH, String FILE_PATH) throws IOException{
		setInputTweetList(FILE_PATH);
		setCenteroidTweetsList(K,CENT_FILE_PATH);
		calcMinJaccadDist();
	}
	
	//Utility function to print the tweet list that is passed!
	public static void printTweets(ArrayList<Tweet> printArrayList){
		for(int i = 0; i < printArrayList.size() ; i++){
			printArrayList.get(i).printTweet();
		}
	}	
	
	//Calculate the Jaccardian distance between the tweets and the centroid tweets!
	public static void calcMinJaccadDist(){
		double MAX_DIST;	long centTweetID,tempTweetID;		Tweet tempTweet,CenteroidTweet;
		boolean isCenteriod = false;		int clustID = -2;

		for(int i = 0; i < overallTweetList.size();i++){
			MAX_DIST = 9999999999.0;	isCenteriod = false;	clustID = -2;
			tempTweet = overallTweetList.get(i);			
			for(int j = 0 ; j < centeriodTweetList.size(); j++){
				CenteroidTweet = centeriodTweetList.get(j);
				centTweetID = CenteroidTweet.getTweetID();
				tempTweetID = tempTweet.getTweetID();
				
				if(tempTweetID == centTweetID){
					isCenteriod = true;
					break;
				}
				else if(tempTweetID != centTweetID){
					double dist = tempTweet.getJaccardianDistance(CenteroidTweet);
					if(MAX_DIST > dist){
						MAX_DIST = dist;
						clustID = CenteroidTweet.getClusterID();
					}
				}
			}
			if(isCenteriod == false){
				tempTweet.setClusterID(clustID);
				tempTweet.setJaccardDist(MAX_DIST);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		int numOfClustersK = Integer.parseInt(args[0]);
		centeroidTweetIDFileName = args[1];
		inputFileName = args[2];
		outputFileName = args[3]; 		
		boolean isStop = false;
		*/
		initializeFirtIteration(25,"C:/Users/Muneer/New folder/Dropbox/UTD/Fall2015/ML/Assignements/Assignment_5/InitialSeeds.txt","C:/Users/Muneer/New folder/Dropbox/UTD/Fall2015/ML/Assignements/Assignment_5/Tweets.json");
		printTweets(overallTweetList);
		//printTweets();
	}
}
