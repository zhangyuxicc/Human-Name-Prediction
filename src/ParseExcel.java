import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ParseExcel {
	
	private ArrayList<String> wordList;
	private ArrayList<String> x0List;
	private ArrayList<String> y0List;
	private ArrayList<String> x1List;
	private ArrayList<String> y1List;
	
	public ParseExcel(String filePath){
		wordList = new ArrayList<String>();
		x0List = new ArrayList<String>();
		y0List = new ArrayList<String>();
		x1List = new ArrayList<String>();
		y1List = new ArrayList<String>();
		Workbook readwb = null;
		try {
			InputStream instream = new FileInputStream(filePath);
			readwb = Workbook.getWorkbook(instream); 
			Sheet sheet = readwb.getSheet(0);
			//get word
			for(int i=0;i<33;i++){
				Cell cell = sheet.getCell(1, i); 
				wordList.add(cell.getContents());
			}
			for(int i=0;i<33;i++){
				Cell cell = sheet.getCell(2, i); 
				x0List.add(cell.getContents());
			}
			for(int i=0;i<33;i++){
				Cell cell = sheet.getCell(3, i); 
				y0List.add(cell.getContents());
			}
			for(int i=0;i<33;i++){
				Cell cell = sheet.getCell(4, i); 
				x1List.add(cell.getContents());
			}
			for(int i=0;i<33;i++){
				Cell cell = sheet.getCell(5, i); 
				y1List.add(cell.getContents());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getWordList(){
		return wordList;
	}
	public ArrayList<String> getX0List(){
		return x0List;
	}
	public ArrayList<String> getY0List(){
		return y0List;
	}
	public ArrayList<String> getX1List(){
		return x1List;
	}
	public ArrayList<String> getY1List(){
		return y1List;
	}
	
}
