import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CreateHTML {
	
	private String htmlPath ="output.html";
	private ArrayList<String> x0List=new ArrayList<String>();
	private ArrayList<String> x1List=new ArrayList<String>();
	private ArrayList<String> y0List=new ArrayList<String>();
	private ArrayList<String> y1List=new ArrayList<String>();
	private ArrayList<String> nameList=new ArrayList<String>();
	private ArrayList<String> DivList=new ArrayList<String>();
	private ArrayList<Integer> WidthList=new ArrayList<Integer>();
	private ArrayList<Integer> HeightList=new ArrayList<Integer>();

	private int number;
	public CreateHTML(ParseExcel parseXLSX){

		this.x0List = parseXLSX.getX0List();
		this.x1List = parseXLSX.getX1List();
		this.y0List = parseXLSX.getY0List();
		this.y1List = parseXLSX.getY1List();
		this.nameList = parseXLSX.getWordList();

		number = nameList.size();
		this.shrink();
		this.setDiv();
		this.setHeight();
		this.setWidth();
	}
	
	/**
	 * shrink the bbox coordinates because most of them are very large
	 * the relative position won't change
	 */
	public void shrink(){
		for(int i=0;i<number;i++){
			int tempx0 = Integer.parseInt(x0List.get(i));
			int tempy0 = Integer.parseInt(y0List.get(i));
			int tempx1 = Integer.parseInt(x1List.get(i));
			int tempy1 = Integer.parseInt(y1List.get(i));
			x0List.set(i, String.valueOf(tempx0/2));
			y0List.set(i, String.valueOf(tempy0/2));
			x1List.set(i, String.valueOf(tempx1/2));
			y1List.set(i, String.valueOf(tempy1/2));
		}
	}
	public void setDiv(){
		for(String value:nameList){
			String strDiv = value.replaceAll("[^a-zA-Z]", "n")+"Div";
			DivList.add(strDiv);
		}
	}
	public void setWidth(){
		for(int i=0;i<x0List.size();i++){
			int x0, x1;
			x0 = Integer.parseInt(x0List.get(i));
			x1 = Integer.parseInt(x1List.get(i));
			WidthList.add(x1-x0);
		}
	}
	public void setHeight(){
		for(int i=0;i<y0List.size();i++){
			int y0, y1;
			y0 = Integer.parseInt(y0List.get(i));
			y1 =Integer.parseInt(y1List.get(i));
			HeightList.add(y1-y0);
		}
	}
	/**
	 * this method is writing a html file for visualization
	 * @throws IOException
	 */
	public void outPut() throws IOException{
		File htmlFile = new File(htmlPath);
		htmlFile.createNewFile();
		FileWriter writer = new FileWriter(htmlFile);
		writer.write("<html>\n"
				+"<head>\n"
				+"<style type=\"text/css\">\n");
		/*for(int i=0;i<number;i++){
			writer.write("."+DivList.get(i)+"\n"
					+"{\n"
					+"position:absolute;\n"
					+"right:"+x0List.get(i)+"px;\n"
					+"bottom:"+y0List.get(i)+"px;\n"
					+"width:"+WidthList.get(i)+"px;\n"
					+"height:"+HeightList.get(i)+"px;\n"
					+"font-size:20px;\n"
					+"}\n");
		}*/
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
		//select a reasonable container size
		writer.write("<div style=\"position:absolute;left:0px;top:0px;width:1200px;height:1800px;border: 3px solid\">");
		for(int i=0;i<number;i++){
			writer.write("<div class=\"dynamicDiv\""
					+" style=\"position:absolute;"
					+"left:"+x0List.get(i)+"px;"
					+"top:"+y0List.get(i)+"px;"
					+"width:"+WidthList.get(i)+"px;"
					+"height:"+HeightList.get(i)+"px;"
					+"text-align:center;font-size:64px;border: 1px solid\">\n"
					+"<span class=\"dynamicSpan\">\n"
					+nameList.get(i)+"\n"
					+"</span>\n"
					+"</div>\n");
		}
		writer.write("</div>");
		writer.write("</body>\n"+"</html>\n");
		writer.flush();
	    writer.close();	
	}
	
	public static void main(String[] args){
		ParseExcel parseXLSX = new ParseExcel("name_position_97.xls");
		CreateHTML writeHTML = new CreateHTML(parseXLSX);
		try {
			writeHTML.outPut();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

