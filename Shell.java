package pers.zmh.shell;
/**
 * java program
 * @author Zhang Minghui
 */
import java.util.Arrays;
import java.util.Scanner; 
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Echo{
	public void work(String s) {
		System.out.println(s);
	}
}

class Grep{
	public void work(String pattern,File path)throws IOException {
		try {
			Pattern pattern2=Pattern.compile(pattern);
			Reader reader=new FileReader(path);
			BufferedReader bufReader=new BufferedReader(reader);
			String string=null;
			while((string=bufReader.readLine()) != null) {
				Matcher matcher=pattern2.matcher(string);
				if(matcher.find()) {
					System.out.println(string);
				}
			}
			bufReader.close();
		} catch (Exception E) {
			System.out.println("grep:"+path.getName()+":没有那个文件或目录");
		}
	}
}

class Ls{
	public void work(File f) {
		if(!f.exists()) {
			System.out.println("ls: 无法访问‘"+f.getName()+"': 没有那个文件或目录");
		}
		else {
			String[] names = f.list();
			System.out.println(Arrays.toString(names));
		}
	}
}

class Pwd{
	public void work(File path){
		System.out.println(path.getAbsolutePath());
	}
}

class Cd{
	public File work(File nowPath,File newpath) {
		if(newpath.exists()) {
			return newpath;
		}
		else {
			System.out.println("cd: 没有那个文件或目录: "+newpath.getName());
			return nowPath;
		}
	}
}

class Cat{
	public void work(File path,String name) {
		File input = new File(path,name);
		try {
			BufferedReader bufReader=new BufferedReader(new FileReader(input));
			String str = null;
			while((str = bufReader.readLine()) != null) {
				System.out.println(str);
			}
			bufReader.close();
		} catch (Exception e) {
			System.out.println("Error");
		}
	}
}
	
class Mkdir{
	public void work(File path,String name) {
		File file=new File(path,name);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
}

class Cp{
	public void work(File src,File dst)throws IOException{
		try {
			FileInputStream srcFis=new FileInputStream(src);
			FileOutputStream dstOut=new FileOutputStream(dst);
			byte[] b=new byte[srcFis.available()];
			srcFis.read(b);
			dstOut.write(b);
			srcFis.close();
			dstOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("cp: 无法获取'" + src + "' 的文件状态: 没有那个文件或目录");
		}
	}
}

class ToHome{
	public  File work(File file) {
		if(file.getName().equals("home")) {
			return file;
		}else {
			file=file.getParentFile();
			if(!file.exists()) {
				return null;
			}
			return work(file);
		}
	}
}

class Path{
	public  File work(File file,String str) {
		if(str.equals(".")) {
			return file;
		}else if(str.equals("..")) {
			return file.getParentFile();
		}else if((str.equals("home") || str.equals("~"))){
			ToHome toHome=new ToHome();
			return toHome.work(file);
		}else if(str.equals("/")) {
			return new File("/");
		}else {
			return new File(file,str);
		}
	}
}

public class Shell {
	public static String path = null;
	public static void main(String[] args)throws IOException{
		System.out.println("请输入路径：（eg：/home/zmh）");
		Scanner input = new Scanner(System.in);//输入路径
		path = input.nextLine();
		File currentpath = new File(path);
		if(!currentpath.exists()) {
			currentpath.mkdirs();
		}
		System.out.println("zmh@"+currentpath.getAbsolutePath()+"$");
		String shell = null;
		String[] strings = null;
		while (true) {
			shell = input.nextLine();
			strings = shell.split(" ");
			switch (strings[0]) {
			case "exit":
				input.close();
				System.exit(0);
				break;
			case "echo":
				Echo echo=new Echo();
				if(strings.length >2) {
					Grep grep=new Grep();
					if(shell.contains("|grep")) {
						grep.work(strings[1], new File(currentpath+File.separator+strings[4]));
					}
				}else {
					echo.work(shell.substring(5,shell.length()));
				}
				break;
			case "grep":
				Grep grep=new Grep();
				grep.work(strings[1], new File(currentpath+ File.separator+strings[2]));
				break;
			case "ls":
				Ls ls=new Ls();
				if(strings.length==1) {
					ls.work(currentpath);
				}else {
					File f=new File(strings[1]);
					ls.work(f);
				}
				break;
			case "pwd":
				Pwd pwd=new Pwd();
				pwd.work(currentpath);
				break;
			case "cd":
				Cd cd=new Cd();
				Path p=new Path();
				File f=p.work(currentpath, strings[1]);
				if(!f.exists()) {
					System.out.println("找不到文件或目录");
				}else {
					currentpath=cd.work(currentpath, f);
				}
				break;
			case "cat":
				Cat cat=new Cat();
				cat.work(currentpath,strings[1]);
				break;
			case "mkdir":
				Mkdir mkdir=new Mkdir();
				mkdir.work(currentpath, strings[1]);
				break;
			case "cp":
				Cp cp=new Cp();
				cp.work(new File(currentpath+ File.separator+strings[1]), new File(currentpath+ File.separator+strings[2]));
				break;
			default:
				System.out.println("zsh: command not found: "+strings[0]);
			}
			System.out.println("zmh@"+currentpath.getAbsolutePath()+"$");
		}
	}
}











