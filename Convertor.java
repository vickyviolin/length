package xw.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Convertor
{
  private String outputFilePath = "src/data/output.txt";
	private String inputFilePath = "src/data/input.txt";
	private String authorInfo = "xiaoweivicky@yeah.net";

	// the number of rules in input.txt
	private int RULE = 6;

	// store the rules into "convertMap"
	private HashMap<String, Double> convertMap = new HashMap<>();

	private String inputString = "";
	private StringBuffer outputString = new StringBuffer("");

	public boolean convert()
	{
		boolean FLAG = false;

		FLAG = readFile();

		String inputs[] = inputString.substring(0, inputString.length() - 1)
				.split("\n");

		loadRule(inputs, RULE);

		calculate(inputs, RULE + 1);

		FLAG = writeFile();

		return FLAG;
	}

	private void loadRule(String inputs[], int mark)
	{
		for (int i = 0; i < mark; i++)
		{
			String rules[] = inputs[i].substring(0, inputs[i].length() - 1)
					.split(" ");
			convertMap.put((String) rules[1], Double.parseDouble(rules[3]));
		}
		System.out.println(convertMap);
	}

	private void calculate(String inputs[], int mark)
	{
		for (int i = mark; i < inputs.length; i++)
		{
			inputs[i] = inputs[i].replace("miles", "mile");
			inputs[i] = inputs[i].replace("yards", "yard");
			inputs[i] = inputs[i].replace("inches", "inch");
			inputs[i] = inputs[i].replace("feet", "foot");
			inputs[i] = inputs[i].replace("faths", "fath");
			inputs[i] = inputs[i].replace("furlongs", "furlong");

			String sums[] = inputs[i].substring(0, inputs[i].length() - 1)
					.split(" ");

			if (sums.length < 2)
			{
				continue;
			}

			double result = Double.parseDouble(sums[0])
					* convertMap.get(sums[1]);

			for (int j = 1; j < (sums.length + 1) / 3; j++)
			{
				double temp = Double.parseDouble(sums[3 * j])
						* convertMap.get(sums[3 * j + 1]);
				if (sums[3 * j - 1].equals("+"))
				{
					// System.out.println(sums[3 * j - 1]);
					result += temp;
				} else if (sums[3 * j - 1].equals("-"))
				{
					// System.out.println(sums[3 * j - 1]);
					result -= temp;
				} else
				{
					// result = result;
				}
			}
			outputString.append("\r\n");
			DecimalFormat df = new DecimalFormat("0.00");
			outputString.append(df.format(result));
			outputString.append(" m");
		}
	}

	private boolean readFile()
	{
		boolean FLAG = false;
		try
		{
			inputString = fileToString(new File(inputFilePath));
		} catch (Exception e)
		{
			e.printStackTrace();
			return FLAG;
		}
		outputString.append(authorInfo);
		outputString.append("\r\n");
		FLAG = true;
		return FLAG;
	}

	private boolean writeFile()
	{
		boolean FLAG = false;
		try
		{
			stringToFile(outputString.toString(), outputFilePath);
		} catch (Exception e)
		{
			e.printStackTrace();
			return FLAG;
		}
		FLAG = true;
		return FLAG;
	}

	public String fileToString(File file) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuffer sbf = new StringBuffer("");
		String line = null;
		while ((line = br.readLine()) != null)
		{
			sbf.append(line).append("\r\n");
		}
		br.close();
		return sbf.toString();
	}

	public void stringToFile(String str, String savePath) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));
		bw.write(str);
		bw.close();
	}

	/**
	 * unit test
	 */
	public static void main(String[] args)
	{
		Convertor convertor = new Convertor();
		boolean ok = convertor.convert();
		System.out.println(ok);
	}

}
