import java.util.*;
import java.lang.*;
import java.io.*;
class parser{
public static void main(String[] arg){
	if(arg.length>0){
		String input_file_name=arg[0];
		File input_file=new File(input_file_name);
		if(input_file.exists()){
			try{
				FileReader reader=new FileReader(input_file);
				String content=new String();
				int a;
				while((a=(int)reader.read())!=-1){
					if(a!=9)
						content+=(char)a+"";
				}
				System.out.println("Packages: "+getPackagesCount(content));
				System.out.println("Classes: "+getClassesCount(content));
				System.out.println("Functions/Blocks: "+getFunctionsCount(content,0));
			}catch(Exception e){System.out.println("Error"+e.toString());}
		}else{
			System.out.println("That file does not exists!");
		}
	}
}

public static int getClassesCount(String content){
	XString xstring=new XString();	
	ArrayList<String> statements=xstring.explode(content,';',true);
	Object obj[]=statements.toArray();
	String testString=new String("");
	int j=0;
	int foundAt=0;
	int counter=0;
	int firstFound=0;
	while(j<Variables.sizeOf(obj)){
		testString=(String)obj[j];
		try{
		if((foundAt=testString.indexOf("class"))!=-1){
			if(scan2(testString,foundAt)){
				firstFound=firstFound>0?firstFound:foundAt;
				counter++;
				
			}
		}
			j++;
		}catch(Exception e){j++;}
		}
	return counter;
}
	public static boolean scan2(String testString,int foundAt){
		boolean foundStartingBraces=false;
		if(testString.substring(foundAt+5,foundAt+6).equals(" "))
		while(foundAt<testString.length()){
			if(!foundStartingBraces){
				if(testString.substring(foundAt,foundAt+1).equals("="))
					break;
				if(testString.substring(foundAt,foundAt+1).equals(")"))
					break;	
				if(testString.substring(foundAt,foundAt+1).equals("{")){
					foundStartingBraces=true;
					break;
				}				
				foundAt++;
			}
		}
		return foundStartingBraces;
	}
	public static int getFunctionsCount(String testString,int start){
	XString xstring=new XString();
	ArrayList<String> parts=xstring.explode(testString,';',true);
	Object obj[]=parts.toArray();
	int j=start;
	int counter=0;
	int i=0;
	char current='\n';
	String clean=new String("");
		while(i<Variables.sizeOf(obj)){
			clean+=XString.removeQuotes((String)obj[i]);
			i++;
		}
		
	counter=countByParanthesis(clean);
	return counter;
	}
	public static int countByParanthesis(String testString){
		boolean insideClass=false;
		boolean insideFunction=false;
		boolean skipNext=false;
		int innerCounter=0;
		int outerCounter=0;
		int dummyCounter=0;
		int j=0;
		char current='\n';
		while(j<testString.length()){
			current=testString.charAt(j);
			try{
			if(((int)testString.charAt(j-1))==61||((int)testString.charAt(j-1))==39)
				skipNext=true;
				}
				catch(Exception e){}
			if(current=='{'&&!skipNext){	
				if(insideClass){
					insideFunction=true;
					insideClass=false;
					innerCounter++;
					
				}
				if(insideFunction){
					innerCounter++;
				}
				if(!insideClass&&!insideFunction){
					insideClass=true;
					
						System.out.println(testString.substring(j-20,j+20)+"<--CLASS ENTRY");
				}
				dummyCounter++;
			}
			if(current=='}'){
				if(!skipNext){
				if(insideClass){
					insideClass=false;
				}
				if(insideFunction){
					 innerCounter--;
					if(innerCounter<1){
						insideFunction=false;
						outerCounter++;
						System.out.println(testString.substring(j-20,j+20)+"<--E");
						insideClass=true;
					}
				}
				}else{skipNext=false;}
			}
			j++;
		}
		return outerCounter;
	}
	public static boolean scanBackward(String testString,int foundAt){
		int current=foundAt;
		String keywords[]={"if","else","try","finally","catch","while","for","switch","class","interface"};
		//System.out.println(testString);
			if(Variables.walkArray(testString,keywords,foundAt-1))
				return true;
			else
				return false;
		
	}
public static int getVariablesCount(String Count){
return 0;
}
public static int getPackagesCount(String content){
	XString xstring=new XString();
	int i,counter=0;
	int j=0;
	i=0;
	String testString=new String("");
	ArrayList<String> statements=xstring.explode(content,';');
	Object obj[]=statements.toArray();
	while(j<Variables.sizeOf(obj)){
		try{
		testString=(String)obj[j];
		if(testString.substring(0,8).contains("import"))
			counter++;
		if(testString.substring(i,8).contains("class")||testString.substring(i,15).contains("interface")||testString.substring(i,12).contains("native")){
			break;
		}
		}catch(Exception e){break;}
		j++;
	}
	return counter;
}
}
class Warning{
	public static void Raise(String error){
		System.out.println("Warning: "+error+"\nStatus: Ignored");
	}
}
/*
	Advance Extended String Class
*/
class XString{
private ArrayList<String> parts=new ArrayList<String>();
private String dirty=new String("");
	public ArrayList<String> explode(String text,char marker){
		int counter=0;
		parts=new ArrayList<String>();
		while(counter<text.length()){
			if(text.charAt(counter)==marker){
				dirty.trim();
				parts.add(dirty);
				dirty="";
			}else{
				dirty+=text.charAt(counter);
			}
			counter++;
		}
		parts.add(dirty);
		return parts;
	}
	public ArrayList<String> explode(String text,char marker,boolean removeCommentText){
		int counter=0;
		parts=new ArrayList<String>();
		while(counter<text.length()){
			if(text.charAt(counter)==marker&&!insideCommentBody(text,counter)){
				dirty.trim();
				parts.add(dirty);
				dirty="";
			}else{
				if(!insideCommentBody(text,counter))
					dirty+=text.charAt(counter);
			}
			counter++;
		}
		parts.add(dirty);
		return parts;
	}
	public static String removeQuotes(String testString){
		String cleanString=new String("");
		int counter=0;
		int currentCode=0;
		char current='\n';
		boolean lookingForSmallerEnd=false;
		boolean lookingForBiggerEnd=false;
		while(counter<testString.length()){
			current=testString.charAt(counter);
			currentCode=(int)testString.charAt(counter);
			if(currentCode==34){
				if(lookingForBiggerEnd){
					if(((int)testString.charAt(counter-1))!=92){
						lookingForBiggerEnd=false;
					}
				}else{
					lookingForBiggerEnd=true;
				}
				}
			if(currentCode==39){
				if(lookingForSmallerEnd){
					if(((int)testString.charAt(counter-1))!=92){
						lookingForSmallerEnd=false;
					}
				}else{
						lookingForSmallerEnd=true;
					}
			}
			if(!lookingForSmallerEnd&&!lookingForBiggerEnd){
				cleanString+=""+(char)currentCode;
			}
			counter++;
		}
		return cleanString;
	}
	public  boolean insideCommentBody(String token,int counter){
		int j=0;
		int lame1,lame2,lame3;
		lame1=lame2=lame3=0;
		boolean notInsideQuotes=true;
		boolean singleQuotes=false;
		boolean doubleQuotes=false;
		boolean lookForEnd=false;
		boolean lookForClose=false;
		int inc=0;
		String testString=token;
		while(j<counter){
			inc=0;
			if(!lookForClose&&!lookForEnd){
				if(testString.substring(j,j+2).indexOf("/*")!=-1&&notInsideQuotes){
					lookForClose=true;
					inc=2;
				}
				if(testString.substring(j,j+2).indexOf("//")!=-1&&notInsideQuotes){
					lookForEnd=true;
					inc=2;
				}
				if(testString.substring(j,j+1).equals("'")&&!singleQuotes){
					singleQuotes=true;
					notInsideQuotes=false;
				}
				if(testString.substring(j,j+2).equals("\"")&&!doubleQuotes){
					doubleQuotes=true;
					notInsideQuotes=false;
				}				
			}else{
				if(lookForClose&&testString.substring(j,j+2).indexOf("*/")!=-1){
					lookForClose=false;
					inc=2;
				}
				if(lookForEnd&&testString.charAt(j)=='\n')
					lookForEnd=false;
			}
			if(!notInsideQuotes){
				if(testString.substring(j,j+1).equals("'")&&singleQuotes){
					singleQuotes=false;
					notInsideQuotes=true;
				}
				if(testString.substring(j,j+2).equals("\"")&&singleQuotes){
					doubleQuotes=false;
					notInsideQuotes=true;
				}
			}
			j=inc>0?j+inc:j+1;
		}
		if(lookForEnd||lookForClose)
			return true;
		else
			return false;		
	}
}
class Variables{
	public static int sizeOf(Object[] array){
		int counter=-1;
		Object dummy;
		try{
			while(true){
				counter++;
				dummy=array[counter];
			}
		}catch(Exception e){return counter;}
	}
	public static boolean isAlpha(char al){
		int code=(int)al;
		if((code>=65&&code<=90)||(code>=97&&code<=122))
			return true;
		else
			return false;
	}
	public static boolean walkArray(String testString,String[] keywords,int foundAt){
		char markers[]={';','}','(','='};
		boolean isKeywordBlock=false;
		for(int i=0;i<sizeOf(keywords);i++){
			try{
			if(testString.substring(foundAt-keywords[i].length(),foundAt+1).equals(keywords[i])){
				if(isAlpha(testString.substring(foundAt-keywords[i].length()-1,foundAt-keywords[i].length()).charAt(0))){
						return false;
				}else{
				for(int j=0;j<3;j++){
					try{
					if(testString.substring(foundAt-keywords[i].length()-1,foundAt-keywords[i].length()).charAt(0)==markers[j])
						return false;
					}catch(Exception e){}
				}
				}
			}
			
				System.out.println(testString.substring(foundAt-keywords[i].length(),foundAt+1));
			}catch(Exception e){}
		}
		return true;
	}
}
