/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config.system;

import config.Block;
import java.util.Vector;

/**
 * This class is used to write parameters for whole eHOST system to disk configure
 * file and load it while system started;
 *
 * @author leng
 */
public class SysConf {

    /**The name of eHOST system configure*/
    public final static String SYS_CONFIGURE = "eHOST.sys";

    /**Load systemm setting variables of eHOST from disk to memory.*/
    public static void loadSystemConfigure()
    {
        // split configure file into blocks
        config.Extracter extracter = new config.Extracter(SYS_CONFIGURE);
        Vector<Block> blocks = extracter.getBlocks();

        // load variable from block into memory
        ParameterGather memloader = new ParameterGather();
        memloader.load(blocks);

    }

    /**save configure to disk*/
    public static void saveSystemConfigure()
    {
        SaveConf saveconf = new SaveConf();
        saveconf.save(SYS_CONFIGURE);
    }
}
