import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ling.CoreLabel;
/**
 * this class shows the training process of name location prediction
 * @author Grace
 *
 */
public class Training {

	private ArrayList<Record> knownLocations = new ArrayList<Record>();
	private ArrayList<PredictLocation> predictions = new ArrayList<PredictLocation>();
	String hocrFile = "";
	private ArrayList<Double> x0List=new ArrayList<Double>();
	private ArrayList<Double> x1List=new ArrayList<Double>();
	private ArrayList<Double> y0List=new ArrayList<Double>();
	private ArrayList<Double> y1List=new ArrayList<Double>();
	private ArrayList<String> wordList=new ArrayList<String>();
	private ArrayList<Double> WidthList=new ArrayList<Double>();
	private ArrayList<Double> HeightList=new ArrayList<Double>();
	
	/**
	 * this method records the name locations obtained from sample files manually
	 */
	public void populateRecords(){
	
		knownLocations.add(new Record("Doug Gray", 1368, 1248, 1614, 1279));
		knownLocations.add(new Record("Heather Devoe", 1370, 1230, 1710, 1260));
		knownLocations.add(new Record("Jamie P", 1372, 1382, 1549, 1413));
		knownLocations.add(new Record("Joseph M", 1203, 1353, 1629, 1390));
		knownLocations.add(new Record("Timothy R schuettpelz", 1193, 1333, 1932, 1371));	
		knownLocations.add(new Record("Dart", 1358, 654, 1460, 680));
		knownLocations.add(new Record("Craig Wiemeri", 255, 1231, 570, 1261));
		knownLocations.add(new Record("Reed Daniel", 110, 1452, 406, 1483));
		knownLocations.add(new Record("Justin Machata", 113, 1454, 464, 1484));
		knownLocations.add(new Record("Muhammad Bukhari", 211, 1894, 408, 1923));		
		knownLocations.add(new Record("Vhayna Romano", 469, 1212, 779, 1267));
		knownLocations.add(new Record("Tanya K Schuettpelz", 598, 1333, 1157, 1371));		
		knownLocations.add(new Record("Joyce A", 850, 1236, 1204, 1284));
		knownLocations.add(new Record("Craig R Jacobs", 851, 1230, 1202, 1279));
		knownLocations.add(new Record("Victoria L Jacobs", 897, 1280, 1292, 1320));
		knownLocations.add(new Record("Yuly Osorio", 847, 1238, 1114, 1276));
		knownLocations.add(new Record("Julio Araujo", 895, 1287, 1166, 1335));
		knownLocations.add(new Record("David Mivshek", 863, 1382, 1185, 1413));		
		knownLocations.add(new Record("Heather L schimmers", 392, 2857, 881, 2888));
		knownLocations.add(new Record("David Anderson", 330, 2761, 676, 2793));
		knownLocations.add(new Record("David Mivshek", 577, 2334, 908, 2392));		
		knownLocations.add(new Record("Jamie P", 963, 2345, 1288, 2385));
		knownLocations.add(new Record("Fredschee", 302, 721, 488, 746));
		knownLocations.add(new Record("Joseph Neilitz", 137, 583, 503, 619));
		knownLocations.add(new Record("Joyce A", 285, 653, 664, 678));
		knownLocations.add(new Record("Yuly Osorio", 286, 652, 565, 688));
		knownLocations.add(new Record("Heather L schimmers", 287, 650, 770, 678));
		knownLocations.add(new Record("Heather L schimmers", 273, 647, 753, 675));
		knownLocations.add(new Record("Craig R Jacobs", 288, 652, 642, 687));
		knownLocations.add(new Record("Victoria L Jacobs", 337, 701, 770, 730));
		knownLocations.add(new Record("Julio Araujo", 338, 702, 642, 739));		
		knownLocations.add(new Record("Manav Anand", 1699, 554, 2004, 586));
		knownLocations.add(new Record("Heather L schimmers", 1575, 711, 2067, 743));		
		knownLocations.add(new Record("Jane E", 1736, 1353, 1882, 1386));
		knownLocations.add(new Record("Adam L", 1896, 1353, 2051, 1386));
			
	}
	
	/**
	 * this method selects some bounding box coordinates and calculate the frequency of human names occurrence
	 */
	public void setPredictions(){
		
		predictions.add(new PredictLocation(1250, 1200, 1700, 1400));
		predictions.add(new PredictLocation(100, 1200, 800, 1500 ));
		predictions.add(new PredictLocation(800, 1200, 1250, 1350));
		predictions.add(new PredictLocation(400, 2350, 900, 2800));
		predictions.add(new PredictLocation(150, 600, 750, 750));
		predictions.add(new PredictLocation(1500, 550, 2100, 750));
		predictions.add(new PredictLocation(1600, 1300, 2100, 1400));
		
		for(PredictLocation p:predictions){
			for(Record r:knownLocations){
				if( p.getX0() <= (r.getX0()+r.getX1())/2 
						&& p.getX1() >= (r.getX0()+r.getX1())/2
						&& p.getY0() <= (r.getY0()+r.getY1())/2
						&& p.getY1() >= (r.getY0()+r.getY1())/2){
					
					p.increment();
				}
			}
		}
//		System.out.println("Human names may be located at:");
		for(PredictLocation p:predictions){
			p.setRate((double)p.getCount()/knownLocations.size());
//			if(p.getRate() > 0.1){
//				System.out.print(p.getX0()+" "+p.getY0()+" "+p.getX1()+" "+p.getY1()+"  ");
//				System.out.println("Probabality: "+((int)(p.getRate()*100))+"%");
//			}
			
		}
	}
	
	public ArrayList<Record> getKnownLocations(){
		return knownLocations;
	}
	
	public ArrayList<PredictLocation> getPredictions(){
		return predictions;
	}
	
	/**
	 * this method will analyze the Tiff file to generate one hocr file and one txt file
	 * human names will be obtained using Stanford NER
	 * @return the name list 
	 */
	private ArrayList<String> analyzeTIFF(){
		
		//get hocr and txt file
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter file name(including absolute path):");
		String inputTiff = in.nextLine();
//		String inputTiff = "/Users/Grace/Desktop/inputTiff/39653_003.tif";		
//		output hocr file and txt file will be in the same folder with the input tiff file
		String outputHocrName = inputTiff.replace(".tif", "");
		hocrFile = outputHocrName;
		String outputTxtName = inputTiff.replace(".tif", "");
		String txtFile = outputTxtName;
		//my tesseract absolute path with one space at the end
		String tessPath="/usr/local/Cellar/tesseract/3.04.01_1/bin/tesseract ";
		String command1 = tessPath + inputTiff + " " + hocrFile+" hocr";
		String command2 = tessPath + inputTiff + " " + txtFile;
		Process p;
		try {
			p = Runtime.getRuntime().exec(command1);
			p = Runtime.getRuntime().exec(command2);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//get txt output content
		String txtOutput = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(txtFile+".txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			txtOutput = sb.toString();
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//using Stanford NER to get names from the text file generated by tesseract
		ArrayList<String> names = new ArrayList<String>();
		AbstractSequenceClassifier<CoreLabel> classifier;
		//give absolute path of english.all.3class.distsim.crf.ser.gz file
		String serializedClassifier = "/Users/Grace/Downloads/stanford-ner-2015-12-09/classifiers/english.all.3class.distsim.crf.ser.gz";
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
			String[] example = txtOutput.split("\n");
			for (String str : example) {
				str = str.replaceAll("&", "&amp;");
				str = str.replaceAll( "&([^;]+(?!(?:\\w|;)))", "&amp;$1" );
				str = str.replaceAll("<", ". &lt;");
				str = str.replaceAll("\"", "&quot;");
				str = str.replaceAll("'","&apos;");
				str = str.replaceAll(">", ". &gt;");
				String xml = "<root>" + classifier.classifyToString(str, "xml", true) + "</root>";
				Document doc = DocumentHelper.parseText(xml);
				Element rootElt = doc.getRootElement();
				@SuppressWarnings("rawtypes")
				Iterator iter = rootElt.elementIterator("wi");
				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					if (recordEle.attributeValue("entity").equals("PERSON")) {
						names.add(recordEle.getText());
					}
				}
			}
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;
	}
	
	/**
	 * this method will get the bounding box coordinates of each human name from hocr file
	 * new info will be added to the records to increase the following prediction accuracy
	 */
	public void analyzeHOCR(){
		
		ArrayList<String> newNames = analyzeTIFF();
		ArrayList<Double> nameX0List = new ArrayList<>();
		ArrayList<Double> nameY0List = new ArrayList<>();
		ArrayList<Double> nameX1List = new ArrayList<>();
		ArrayList<Double> nameY1List = new ArrayList<>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			System.out.println("Analyzing hocr file....");
			dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(new File(hocrFile+".hocr"));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("span");
			for(int i=0;i< nList.getLength();i++){
				org.w3c.dom.Node node = nList.item(i);
				org.w3c.dom.Element ele = (org.w3c.dom.Element) node;
				if(ele.getAttribute("class").equals("ocrx_word")){
					wordList.add(ele.getTextContent());
					String[] coordinate = ele.getAttribute("title").split(" ");
					double tempX0 = Double.parseDouble(coordinate[1]);
					double tempY0 = Double.parseDouble(coordinate[2]);
					double tempX1 = Double.parseDouble(coordinate[3]);
					double tempY1 = Double.parseDouble(coordinate[4].replace(";", ""));
					x0List.add(tempX0);
					y0List.add(tempY0);
					x1List.add(tempX1);
					y1List.add(tempY1);	
					for(String name: newNames){
						if(ele.getTextContent().equals(name)){
							nameX0List.add(tempX0);
							nameY0List.add(tempY0);
							nameX1List.add(tempX1);
							nameY1List.add(tempY1);
//							boolean isCorrect = false;
//							System.out.print("Location: "+ tempX0+" "+tempY0+" "+tempX1+" "+tempY1+" ");
//							for(PredictLocation p: predictions){
//								if(p.getRate()>0.1){
//									if( p.getX0() <= (tempX0+tempX1)/2 
//											&& p.getX1() >= (tempX0+tempX1)/2
//											&& p.getY0() <= (tempY0+tempY1)/2
//											&& p.getY1() >= (tempY0+tempY1)/2){
//										isCorrect = true;				
//									}
//								}
//							}
//							if(isCorrect)System.out.println("Prediction correct!");
//							else System.out.println("Oops Prediction wrong!");
						}
					}		
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//use new names and their locations to update the database, increase accuracy of next prediction
		for(int i=0; i<newNames.size();i++){
			knownLocations.add(new Record(newNames.get(i),nameX0List.get(i),nameX1List.get(i), nameY0List.get(i),nameY1List.get(i)));
			for(PredictLocation p:predictions){
				
				if( p.getX0() <= (nameX0List.get(i)+ nameX1List.get(i))/2 
						&& p.getX1() >= (nameX0List.get(i)+nameX1List.get(i))/2
						&& p.getY0() <= (nameY0List.get(i)+nameY1List.get(i))/2
						&& p.getY1() >= (nameY0List.get(i)+nameY1List.get(i))/2){
					
					p.increment();
				}			
			}
		}
		
		for(PredictLocation p:predictions){
			p.setRate((double)p.getCount()/knownLocations.size());
		}
	
	}
	

	/**
	 * this method is writing a html file for prediction visualization
	 * @throws IOException
	 */
	public void writeHTML(String htmlPath) throws IOException{
		
		File htmlFile = new File(htmlPath);
		htmlFile.createNewFile();
		shrink();
		setWidthAndHeight();
		FileWriter writer = new FileWriter(htmlFile);
		System.out.println("start creating HTML....");
		writer.write("<html>\n"
				+"<head>\n"
				+"<style type=\"text/css\">\n");

		writer.write("</style>\n"
				+"<script type=\"text/javascript\">\n"
				+"function shrink()\n"
				+"{\n"
				+"var textDivs=document.getElementsByClassName(\"dynamicDiv\");\n"
				+"var textDivsLength = textDivs.length;\n"
				+"for(var i=0; i<textDivsLength; i++) {\n"
				+"var textDiv = textDivs[i];\n"
				+"var textSpan = textDiv.getElementsByClassName(\"dynamicSpan\")[0];\n"
				+"textSpan.style.fontSize = 64;\n"
				+"while(textSpan.offsetHeight > textDiv.offsetHeight)\n"
				+"{\n"
				+"textSpan.style.fontSize = parseInt(textSpan.style.fontSize) - 1;\n"
				+"}\n"
				+"}\n"
				+"}\n"
				+"</script>\n");
		writer.write("</head>\n");
		writer.write("<body onload=\"shrink()\">\n");
		//highlight the predict area
		for(PredictLocation p:predictions){
			if(p.getRate()>0.1){
				writer.write("<div class=\"dynamicDiv\""
						+" style=\"position:absolute;"
						+"left:"+p.getX0()/2+"px;"
						+"top:"+p.getY0()/2+"px;"
						+"width:"+(p.getX1()/2 - p.getX0()/2)+"px;"
						+"height:"+(p.getY1()/2 - p.getY0()/2)+"px;"
						+"text-align:center;font-size:64px;border: 2px solid red\">\n"
						+"<span class=\"dynamicSpan\">\n"
						+""+"\n"
						+"</span>\n"
						+"</div>\n");
			}
		}
		//write all the elements
		for(int i=0;i<wordList.size();i++){
			writer.write("<div class=\"dynamicDiv\""
					+" style=\"position:absolute;"
					+"left:"+x0List.get(i)+"px;"
					+"top:"+y0List.get(i)+"px;"
					+"width:"+WidthList.get(i)+"px;"
					+"height:"+HeightList.get(i)+"px;"
					+"text-align:center;font-size:64px\">\n"
					+"<span class=\"dynamicSpan\">\n"
					+wordList.get(i)+"\n"
					+"</span>\n"
					+"</div>\n");
		}
		writer.write("</div>");
		writer.write("</body>\n"+"</html>\n");
		writer.flush();
	    writer.close();	
		System.out.println("HTML created!");
	}

	/**
	 * shrink the bbox coordinates because most of them are very large
	 * the relative position won't change
	 */
	private void shrink(){
		for(int i=0;i<wordList.size();i++){
			double tempx0 = x0List.get(i);
			double tempy0 = y0List.get(i);
			double tempx1 = x1List.get(i);
			double tempy1 = y1List.get(i);
			x0List.set(i, tempx0/2);
			y0List.set(i, tempy0/2);
			x1List.set(i, tempx1/2);
			y1List.set(i, tempy1/2);
		}
	}
	
	private void setWidthAndHeight(){
		for(int i=0;i<x0List.size();i++){
			WidthList.add(x1List.get(i)-x0List.get(i));
			HeightList.add(y1List.get(i)-y0List.get(i));
		}
	}
	

	public static void main(String[] args){
		Training t = new Training();
		t.populateRecords();
		t.setPredictions();
		t.analyzeHOCR();
		try {
			t.writeHTML("predict.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
