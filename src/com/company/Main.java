package com.company;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.awt.*;
import java.io.*;

public class Main
{


	protected static int currentIndex;
	protected static String lexiamsOutput = new String();
	protected static StringArray identifiers = new StringArray();

	private static int getIDNumber(String str)
	{
		for(int i = 0 ; i < identifiers.getSize(); i ++)
		{
			if(identifiers.get(i).equals(str))
				return i;
		}
		identifiers.add(str);
		return identifiers.getSize()-1;
	}
	private static void removeSingleLineComment(String str)
	{
		int tempIndex = currentIndex;
		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("//"))
			{
				//System.out.println();
				while(tempIndex < str.length() && str.charAt(tempIndex) != '\n')
				{
					//System.out.print(str.charAt(tempIndex));
					tempIndex++;
				}
				//System.out.println();
				currentIndex = tempIndex;
			}
		}
	}
	private static void removeMultiLineComment(String str)
	{

		if ((str.length() - (currentIndex + 1)) > 1)
		{
			if (str.charAt(currentIndex) =='/' && str.charAt(currentIndex+1) == '*')
			{
				currentIndex += 2;
				while(currentIndex < str.length()-1)
				{
					if(str.charAt(currentIndex) == '*' && str.charAt(currentIndex+1) =='/')
					{
						currentIndex = currentIndex + 2;
						return;
					}

					currentIndex++;
				}
			}

		}
	}
	private static String checkId(String str)
	{
		int status = 0;
		int tempIndex = currentIndex;
		while (tempIndex < str.length())
		{
			switch (status)
			{
				case 0:
				{
					if (Character.isAlphabetic(str.charAt(tempIndex)))
					{
						status = 1;
						tempIndex++;
						break;
					} else
						return null;
				}
				case 1:
				{
					if ((Character.isAlphabetic(str.charAt(tempIndex)) || Character.isDigit(str.charAt(tempIndex))))
					{
						status = 1;
						tempIndex++;
						break;
					} else
					{
						status = 2;
						break;
					}
				}
				case 2:
				{
					String varName = str.substring(currentIndex, tempIndex);
					currentIndex = tempIndex;
					return "(ID," + String.valueOf(getIDNumber(varName)) + ")";
				}
			}
		}
		return null;
	}
	private static String checkInt(String str)
	{
		if ((str.length() - (currentIndex + 3)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 3).equals("int") && !Character.isAlphabetic(str.charAt(currentIndex+3)))
			{
				currentIndex += 3;
				return "(INT,^)";
			}
		}
		return null;
	}
	private static String checkChar(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 4).equals("char") && !Character.isAlphabetic(str.charAt(currentIndex+4)))
			{
				currentIndex += 4;
				return "(CHAR,^)";
			}

		}
		return null;
	}
	private static String checkVoid(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1)
		{

			if (str.substring(currentIndex, currentIndex + 4).equals("void") && !Character.isAlphabetic(str.charAt(currentIndex+4)))
			{
				currentIndex += 4;
				return "(VOID,^)";
			}
		}
		return null;
	}
	public static String checkIf(String str)
	{
		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("if") && !Character.isAlphabetic(str.charAt(currentIndex+2)))
			{
				currentIndex += 2;
				return "(IF,^)";
			}
		}
		return null;
	}
	public static String checkElse(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1 && !Character.isAlphabetic(str.charAt(currentIndex+4)))
		{
			if (str.substring(currentIndex, currentIndex + 4).equals("else"))
			{
				currentIndex += 4;
				return "(ELSE,^)";
			}
		}
		return null;
	}
	public static String checkWhile(String str)
	{
		if ((str.length() - (currentIndex + 5)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 5).equals("while") && !Character.isAlphabetic(str.charAt(currentIndex+5)))
			{
				currentIndex += 5;
				return "(WHILE,^)";
			}
		}
		return null;
	}
	public static String checkRet(String str)
	{
		if ((str.length() - (currentIndex + 6)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 6).equals("return") && !Character.isAlphabetic(str.charAt(currentIndex+6)))
			{
				currentIndex += 6;
				return "(RET,^)";
			}
		}
		return null;
	}
	public static String checkOut(String str)
	{
		if ((str.length() - (currentIndex + 3)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 3).equals("out") && !Character.isAlphabetic(str.charAt(currentIndex+3)))
			{
				currentIndex += 3;
				return "(OUT,^)";
			}
		}
		return null;
	}
	public static String checkIn(String str)
	{
		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("in") && !Character.isAlphabetic(str.charAt(currentIndex+2)))
			{
				currentIndex += 2;
				return "(IN,^)";
			}
		}
		return null;
	}
	public static String check_G_OR_GE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals(">="))
			{
				currentIndex += 2;
				return "(RO,GE)";
			}
			else if(str.charAt(currentIndex) == '>')
			{
				currentIndex ++;
				return "(RO,G)";
			}
		}
		return null;

	}
	public static String check_L_OR_LE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("<="))
			{
				currentIndex += 2;
				return "(RO,LE)";
			}
			else if(str.charAt(currentIndex) == '<')
			{
				currentIndex ++;
				return "(RO,L)";
			}
		}
		return null;
	}
	public static String check_E_OR_NE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("=="))
			{
				currentIndex += 2;
				return "(RO,E)";
			}
			else if(str.substring(currentIndex, currentIndex + 2).equals("<>"))
			{
				currentIndex += 2;
				return "(RO,NE)";
			}
		}
		return null;

	}
	public static String checkAdd(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{
			if (str.charAt(currentIndex) == '+')
			{
				currentIndex++;
				return "(\'+\',^)";
			}
		}
		return null;
	}
	public static String checkSub(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '-')
			{
				currentIndex++;
				return "(\'-\',^)";
			}
		}
		return null;
	}
	public static String checkDIV(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '/')
			{
				currentIndex++;
				return "(\'/\',^)";
			}
		}
		return null;
	}
	public static String checkMUL(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '*')
			{
				currentIndex++;
				return "(\'*\',^)";
			}
		}
		return null;
	}
	public static String checkNum(String str)
	{
		int tempIndex = currentIndex;
		int status = 0;
		while(tempIndex < str.length())
		{
			switch (status)
			{
				case 0:
				{
					if(Character.isDigit(str.charAt(tempIndex)))
					{
						status = 1;
						tempIndex++;
						break;
					}
					else
					{
						return null;
					}
				}
				case 1:
				{
					if(Character.isDigit(str.charAt(tempIndex)))
					{
						tempIndex++;
						break;
					}
					else
					{
						status = 2;
						break;
					}
				}
				case 2:
				{
					String retStr = "(NUM, " + str.substring(currentIndex,tempIndex) + ")";
					currentIndex = tempIndex;
					return retStr;
				}
			}

		}
		return null;
	}
	public static String checkLiteralCons(String str)
	{
		int status = 0;
		int tempIndex = currentIndex;
		while(tempIndex < str.length())
		{
			switch (status)
			{
				case 0:
				{
					if(str.charAt(tempIndex) == '\'')
					{
						status = 1;
						tempIndex++;
						break;
					}
					else
						return null;
				}
				case 1:
				{
					if(Character.isAlphabetic(str.charAt(tempIndex)))
					{
						tempIndex++;
						break;
					}
					else
					{
						status = 2;
						break;
					}
				}
				case 2:
				{
					if(str.charAt(tempIndex) == '\'')
					{
						tempIndex++;
						String retStr = "(LITERAL_CONS, " + str.substring(currentIndex,tempIndex) + ")";
						currentIndex = tempIndex;
						return  retStr;
					}
					else
						return null;
				}
			}
		}
		return null;
	}
	public static String checkAssignment(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '=')
			{
				currentIndex++;
				return "(\'=\',^)";
			}
		}
		return null;
	}
	public static String checkComma(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ',')
			{
				currentIndex++;
				return "(\',\',^)";
			}
		}
		return null;
	}
	public static String checkSemiColon(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ';')
			{
				currentIndex++;
				return "(\';\',^)";
			}
		}
		return null;
	}
	public static String checkOpenPren(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '(')
			{
				currentIndex++;
				return "(\'(\',^)";
			}
		}
		return null;
	}
	public static String checkClosePren(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ')')
			{
				currentIndex++;
				return "(\')\',^)";
			}
		}
		return null;
	}
	public static String checkOpenBraces(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '{')
			{
				currentIndex++;
				return "(\'{\',^)";
			}
		}
		return null;
	}
	public static String checkCloseBraces(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '}')
			{
				currentIndex++;
				return "(\'}\',^)";
			}
		}
		return null;
	}
	public static String checkOpenSqBracket(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '[')
			{
				currentIndex++;
				return "(\'[\',^)";
			}
		}
		return null;
	}
	public static String checkCloseSqBrackets(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ']')
			{
				currentIndex++;
				return "(\']\',^)";
			}
		}
		return null;
	}
	public static void writeOutputs()
	{
		//writing lexiams
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("words.txt"), "utf-8")))
		{
			writer.write(lexiamsOutput);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// writing identifiers
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("symboltable.txt"), "utf-8")))
		{
			for(int i = 0; i < identifiers.getSize(); i++)
			{
				writer.write(String.valueOf(i) + ": "+ identifiers.get(i) +"\n" );
			}
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	public static void lexAnalyser(String str)
	{
		currentIndex = 0;
		while (currentIndex < str.length())
		{
			String outStr;

			//ignore comments in the input program
			removeSingleLineComment(str);
			removeMultiLineComment(str);

			if ((outStr = checkVoid(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkIf(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkWhile(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkRet(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkOut(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkIn(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkElse(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkInt(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkChar(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_E_OR_NE(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_G_OR_GE(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_L_OR_LE(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkDIV(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkMUL(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkSub(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkAdd(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkId(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkNum(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkLiteralCons(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenPren(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenBraces(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenSqBracket(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkClosePren(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkCloseBraces(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkCloseSqBrackets(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkAssignment(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkComma(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkSemiColon(str)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}
		else if (str.charAt(currentIndex) != ' ' && str.charAt(currentIndex) != '\f'
					&& str.charAt(currentIndex) != '\t' && str.charAt(currentIndex) != '\n'
					&& str.charAt(currentIndex) != '\r' && !Character.isSpaceChar(str.charAt(currentIndex))
					&& !Character.isWhitespace(str.charAt(currentIndex)))
			{
				System.out.println("Invalid Character found at " + String.valueOf(currentIndex));
				System.out.println(str.charAt(currentIndex));
				return;
			}
			else
			{
				currentIndex++;
			}
			if(outStr!= null)
				System.out.println(outStr);
		}
	}
	public static void main(String[] args)
	{

		String fileName = "input.txt";
		String inputString = new String();
		try
		{
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = new String();

			while ((line = bufferedReader.readLine()) != null)
			{
				inputString = inputString.concat(line + "\n");
			}

			System.out.println(inputString);

			lexAnalyser(inputString);

			writeOutputs();

			bufferedReader.close();

		} catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex)
		{
			System.out.println("Error reading file '" + fileName + "'");
		}
	}


}
