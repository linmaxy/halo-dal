package halo.dal.partition;

import halo.dal.partition.analysis.SQLInfo;

/**
 * 数据分区分析器，通过此分析器可以分析表所在的具体的数据库与数据表名称
 * 
 * @author akwei
 */
public interface DALPartitionParser {

    /**
     * 根据内容进行分析，创建表的分区信息
     * 
     * @param tableLogicName
     *            逻辑表名称，也将会成为表的别名
     * @param sqlInfo
     *            参考 {@link SQLInfo}
     * @return
     */
    DALPartitionTableInfo parse(String tableLogicName, SQLInfo sqlInfo);
}
