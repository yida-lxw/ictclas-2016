package code;

import java.util.ArrayList;
import java.util.List;


import com.ictclas.analysis.analyzer.Word;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"E:/git-space/ictclas-2016/Data/NLPIR", CLibrary.class);

		// 初始化函数声明：sDataPath是初始化路径地址，
		// 包括核心词库和配置文件的路径，encoding为输入字符的编码格式
		public int NLPIR_Init(byte[] sDataPath, int encoding,
							  byte[] sLicenceCode);
		// 分词函数声明：sSrc为待分字符串，bPOSTagged=0表示不进行词性标注，
		// bPOSTagged=1表示进行词性标注
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		//获取关键词
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
										boolean bWeightOut);
		public void NLPIR_Exit();
	}

	public static void main(String[] args) throws Exception {
		String argu = "";
		//UTF-8:1 GBK:0，BIG5:2 含繁体字的GBK:3
		String system_charset = "UTF-8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu
				.getBytes(system_charset), charset_type, "0"
				.getBytes(system_charset));

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}

		String sInput = "//据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";

		String returnText = null;
		try {
			returnText = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
			System.out.println("分词结果为： " + returnText);

			/*String keyWords = CLibrary.Instance.NLPIR_GetKeyWords(sInput, Integer.MAX_VALUE,true);
			System.out.println("关键词提取结果是：" + keyWords);*/

			String[] array = returnText.split(" ");
			int count = array.length;
			int start = 0;
			List<Word> words = new ArrayList<Word>();
			for(int i=0; i <count; i++) {
				int position = i+1;
				String item = array[i];
				String[] arr = item.split("/");
				String text = arr[0];
				String type = arr[1];
				int length = text.length();
				Word word = new Word(text,type,position,start,length);
				words.add(word);
				start += length;
			}

			for(Word word : words) {
				System.out.println(word);
			}

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
