package com.ziroom.filetransport.model;

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
 * @Date Created in 2018年04月27日 21:10
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private Terminal terminal;
    private String file;
}
