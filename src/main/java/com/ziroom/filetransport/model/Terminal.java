package com.ziroom.filetransport.model;

import com.ziroom.filetransport.constant.SystemParamConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p></p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author renhy
 * @version 1.0
 * @Date Created in 2018年04月25日 16:20
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Terminal {
    private String name;
    private String ip;
    private Integer port = SystemParamConstant.WORK_PORT;

    public Terminal(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }
}
