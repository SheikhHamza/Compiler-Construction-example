package com.company;

import com.sun.xml.internal.fastinfoset.util.StringArray;

import java.io.*;
import java.util.ArrayList;


public class Lexiam
{
    private String inputProgram = new String();
	private int currentIndex;
	private String lexiamsOutput = new String();
	private StringArray identifiers = new StringArray();
	private ArrayList<TokenLex> token_and_lexiams = new ArrayList<TokenLex>();

	private int getIDNumber(String str)
	{
		for(int i = 0 ; i < identifiers.getSize(); i ++)
		{
			if(identifiers.get(i).equals(str))
				return i;
		}
		identifiers.add(str);
		return identifiers.getSize()-1;
	}
	private void removeSingleLineComment(String str)
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
	private void removeMultiLineComment(String str)
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
	private String checkId(String str)
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
					String output = "(ID," + String.valueOf(getIDNumber(varName)) + ")";
					TokenLex temp = new TokenLex("ID",identifiers.get(getIDNumber(varName)));
					token_and_lexiams.add(temp);
					return output;
				}
			}
		}
		return null;
	}
	private String checkInt(String str)
	{
		if ((str.length() - (currentIndex + 3)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 3).equals("int") && !Character.isAlphabetic(str.charAt(currentIndex+3)))
			{
				currentIndex += 3;
				TokenLex temp = new TokenLex("INT","^");
				token_and_lexiams.add(temp);
				return "(INT,^)";
			}
		}
		return null;
	}
	private String checkIntArr(String str)
	{

		if ((str.length() - (currentIndex + 5)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 5).equals("int[]") && !Character.isAlphabetic(str.charAt(currentIndex+5)))
			{
				currentIndex += 5;
				TokenLex temp = new TokenLex("INT[]","^");
				token_and_lexiams.add(temp);
				return "(INT,^)";
			}
		}
		return null;
	}
	private String checkCharArr(String str)
	{
		if ((str.length() - (currentIndex + 6)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 6).equals("char") && !Character.isAlphabetic(str.charAt(currentIndex+6)))
			{
				currentIndex += 6;
				TokenLex temp = new TokenLex("CHAR[]","^");
				token_and_lexiams.add(temp);
				return "(CHAR[],^)";
			}

		}
		return null;
	}
	private String checkChar(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 4).equals("char") && !Character.isAlphabetic(str.charAt(currentIndex+4)))
			{
				currentIndex += 4;
				TokenLex temp = new TokenLex("CHAR","^");
				token_and_lexiams.add(temp);
				return "(CHAR,^)";
			}

		}
		return null;
	}
	private String checkVoid(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1)
		{

			if (str.substring(currentIndex, currentIndex + 4).equals("void") && !Character.isAlphabetic(str.charAt(currentIndex+4)))
			{
				currentIndex += 4;
				TokenLex temp = new TokenLex("VOID","^");
				token_and_lexiams.add(temp);
				return "(VOID,^)";
			}
		}
		return null;
	}
	private String checkIf(String str)
	{
		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("if") && !Character.isAlphabetic(str.charAt(currentIndex+2)))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("IF","^");
				token_and_lexiams.add(temp);
				return "(IF,^)";
			}
		}
		return null;
	}
	private String checkElse(String str)
	{
		if ((str.length() - (currentIndex + 4)) > 1 && !Character.isAlphabetic(str.charAt(currentIndex+4)))
		{
			if (str.substring(currentIndex, currentIndex + 4).equals("else"))
			{
				currentIndex += 4;
				TokenLex temp = new TokenLex("ELSE","^");
				token_and_lexiams.add(temp);
				return "(ELSE,^)";
			}
		}
		return null;
	}
	private String checkWhile(String str)
	{
		if ((str.length() - (currentIndex + 5)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 5).equals("while") && !Character.isAlphabetic(str.charAt(currentIndex+5)))
			{
				currentIndex += 5;
				TokenLex temp = new TokenLex("WHILE","^");
				token_and_lexiams.add(temp);
				return "(WHILE,^)";
			}
		}
		return null;
	}
	private String checkRet(String str)
	{
		if ((str.length() - (currentIndex + 6)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 6).equals("return") && !Character.isAlphabetic(str.charAt(currentIndex+6)))
			{
				currentIndex += 6;
				TokenLex temp = new TokenLex("RET","^");
				token_and_lexiams.add(temp);
				return "(RET,^)";
			}
		}
		return null;
	}
	private String checkOut(String str)
	{
		if ((str.length() - (currentIndex + 3)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 3).equals("out") && !Character.isAlphabetic(str.charAt(currentIndex+3)))
			{
				currentIndex += 3;
				TokenLex temp = new TokenLex("OUT","^");
				token_and_lexiams.add(temp);
				return "(OUT,^)";
			}
		}
		return null;
	}
	private String checkIn(String str)
	{
		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("in") && !Character.isAlphabetic(str.charAt(currentIndex+2)))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("IN","^");
				token_and_lexiams.add(temp);
				return "(IN,^)";
			}
		}
		return null;
	}
	private String check_G_OR_GE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals(">="))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("RO",">=");
				token_and_lexiams.add(temp);
				return "(RO,GE)";
			}
			else if(str.charAt(currentIndex) == '>')
			{
				currentIndex ++;
				TokenLex temp = new TokenLex("RO",">");
				token_and_lexiams.add(temp);
				return "(RO,G)";
			}
		}
		return null;

	}
	private String check_L_OR_LE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("<="))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("RO","<=");
				token_and_lexiams.add(temp);
				return "(RO,LE)";
			}
			else if(str.charAt(currentIndex) == '<')
			{
				currentIndex ++;
				TokenLex temp = new TokenLex("RO","<");
				token_and_lexiams.add(temp);
				return "(RO,L)";
			}
		}
		return null;
	}
	private String check_E_OR_NE(String str)
	{

		if ((str.length() - (currentIndex + 2)) > 1)
		{
			if (str.substring(currentIndex, currentIndex + 2).equals("=="))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("RO","==");
				token_and_lexiams.add(temp);
				return "(RO,E)";
			}
			else if(str.substring(currentIndex, currentIndex + 2).equals("<>"))
			{
				currentIndex += 2;
				TokenLex temp = new TokenLex("RO","<>");
				token_and_lexiams.add(temp);
				return "(RO,NE)";
			}
		}
		return null;

	}
	private String checkAdd(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{
			if (str.charAt(currentIndex) == '+')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("+","^");
				token_and_lexiams.add(temp);
				return "(\'+\',^)";
			}
		}
		return null;
	}
	private String checkSub(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '-')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("-","^");
				token_and_lexiams.add(temp);
				return "(\'-\',^)";
			}
		}
		return null;
	}
	private String checkDIV(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '/')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("/","^");
				token_and_lexiams.add(temp);
				return "(\'/\',^)";
			}
		}
		return null;
	}
	private String checkMUL(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '*')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("*","^");
				token_and_lexiams.add(temp);
				return "(\'*\',^)";
			}
		}
		return null;
	}
	private String checkNum(String str)
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
					String output = "(NUM, " + str.substring(currentIndex,tempIndex) + ")";
					TokenLex temp = new TokenLex("NUM",str.substring(currentIndex,tempIndex));
					token_and_lexiams.add(temp);
					currentIndex = tempIndex;
					return output;
				}
			}

		}
		return null;
	}
	private String checkLiteralCons(String str)
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
						String output = "(LITERAL_CONS, " + str.substring(currentIndex,tempIndex) + ")";
						TokenLex temp = new TokenLex("LITERAL_CONS",str.substring(currentIndex,tempIndex));
						token_and_lexiams.add(temp);
						currentIndex = tempIndex;
						return  output;
					}
					else
						return null;
				}
			}
		}
		return null;
	}
	private String checkAssignment(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '=')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("=","^");
				token_and_lexiams.add(temp);
				return "(\'=\',^)";
			}
		}
		return null;
	}
	private String checkComma(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ',')
			{
				currentIndex++;
				TokenLex temp = new TokenLex(",","^");
				token_and_lexiams.add(temp);
				return "(\',\',^)";
			}
		}
		return null;
	}
	private String checkSemiColon(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ';')
			{
				currentIndex++;
				TokenLex temp = new TokenLex(";","^");
				token_and_lexiams.add(temp);
				return "(\';\',^)";
			}
		}
		return null;
	}
	private String checkOpenPren(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '(')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("(","^");
				token_and_lexiams.add(temp);
				return "(\'(\',^)";
			}
		}
		return null;
	}
	private String checkClosePren(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ')')
			{
				currentIndex++;
				TokenLex temp = new TokenLex(")","^");
				token_and_lexiams.add(temp);
				return "(\')\',^)";
			}
		}
		return null;
	}
	private String checkOpenBraces(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '{')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("{","^");
				token_and_lexiams.add(temp);
				return "(\'{\',^)";
			}
		}
		return null;
	}
	private String checkCloseBraces(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '}')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("}","^");
				token_and_lexiams.add(temp);
				return "(\'}\',^)";
			}
		}
		return null;
	}
	private String checkOpenSqBracket(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == '[')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("[","^");
				token_and_lexiams.add(temp);
				return "(\'[\',^)";
			}
		}
		return null;
	}
	private String checkCloseSqBrackets(String str)
	{
		if ((str.length() - (currentIndex )) > 1)
		{

			if (str.charAt(currentIndex) == ']')
			{
				currentIndex++;
				TokenLex temp = new TokenLex("]","^");
				token_and_lexiams.add(temp);
				return "(\']\',^)";
			}
		}
		return null;
	}
	public void readFile(String str)
	{
		try
		{
			FileReader fileReader = new FileReader(str);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = new String();

			while ((line = bufferedReader.readLine()) != null)
			{
				inputProgram = inputProgram.concat(line + "\n");
			}

			System.out.println(inputProgram);

			bufferedReader.close();

		} catch (FileNotFoundException ex)
		{
			System.out.println("Unable to open file '" + str + "'");
		} catch (IOException ex)
		{
			System.out.println("Error reading file '" + str + "'");
		}
	}
	public void writeOutputs()
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
	public void lexAnalyser()
	{
		currentIndex = 0;
		while (currentIndex < inputProgram.length())
		{
			String outStr;

			//ignore comments in the input program
			removeSingleLineComment(inputProgram);
			removeMultiLineComment(inputProgram);

			if ((outStr = checkVoid(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkIf(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkWhile(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkRet(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkOut(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			} else if ((outStr = checkIn(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkElse(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkIntArr(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkInt(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkCharArr(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}
			else if ((outStr = checkChar(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_E_OR_NE(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_G_OR_GE(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = check_L_OR_LE(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkDIV(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkMUL(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkSub(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkAdd(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkId(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkNum(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkLiteralCons(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenPren(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenBraces(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkOpenSqBracket(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkClosePren(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkCloseBraces(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkCloseSqBrackets(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkAssignment(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkComma(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}else if ((outStr = checkSemiColon(inputProgram)) != null)
			{
				lexiamsOutput = lexiamsOutput.concat(outStr +  "\n");
			}
			else if (inputProgram.charAt(currentIndex) != ' ' && inputProgram.charAt(currentIndex) != '\f'
					&& inputProgram.charAt(currentIndex) != '\t' && inputProgram.charAt(currentIndex) != '\n'
					&& inputProgram.charAt(currentIndex) != '\r' && !Character.isSpaceChar(inputProgram.charAt(currentIndex))
					&& !Character.isWhitespace(inputProgram.charAt(currentIndex)))
			{
				System.out.println("Invalid Character found at " + String.valueOf(currentIndex));
				System.out.println(inputProgram.charAt(currentIndex));
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

	public ArrayList<TokenLex> getToken_and_lexiams()
	{
		return token_and_lexiams;
	}
}
