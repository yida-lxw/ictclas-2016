package com.ictclas.dic;
import com.ictclas.core.NlpirLib;

/**
 * Created by Lanxiaowei
 * 用户字典管理
 */
public class UserDicManager {
    /**
     * 添加新词到用户字典(添加到内存中)
     * @param newWord
     * @return true表示添加成功 false表示添加失败
     */
    public static boolean addNewWord(String newWord) {
        return NlpirLib.Instance.NLPIR_AddUserWord(newWord) == 1;
    }

    /**
     * 从用户字典中删除一个词汇(内存中删除)
     * @param word
     * @return  true表示删除成功 false表示删除失败
     */
    public static boolean delUsrWord(String word) {
        return NlpirLib.Instance.NLPIR_DelUsrWord(word) == 1;
    }

    /**
     * 写入用户字典(持久化硬盘上)
     * @return true表示写入成功 false表示写入失败
     */
    public static boolean saveToUsrDic() {
        return NlpirLib.Instance.NLPIR_SaveTheUsrDic() == 1;
    }

    /**
     * 导入用户自定义词典
     * @param dicPath  自定义词典的加载路径
     * @param overwrite true表示覆盖当前自定义词典 false表示追加到当前自定义词典的末尾
     * @return  true表示导入成功 false表示导入失败
     */
    public static boolean importUserDict(String dicPath, boolean overwrite) {
        return NlpirLib.Instance.NLPIR_ImportUserDict(dicPath,overwrite) > 0;
    }
}
