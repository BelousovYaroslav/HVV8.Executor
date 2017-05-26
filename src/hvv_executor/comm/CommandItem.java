/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvv_executor.comm;

import hvv_executor.executors.AStatementExeThread;
import java.util.LinkedList;

/**
 *
 * @author yaroslav
 */
public class CommandItem {
    private final AStatementExeThread m_processor;
    public AStatementExeThread GetProcessor() { return m_processor;}
    
    private String m_strCommandId;
    /**
     * Get ID for current processing command transaction
     * @return
     *   id
     */
    public String GetCommandId() { return m_strCommandId; }
    public void SetCommandId( String strId) { m_strCommandId = strId; }
    
    private final LinkedList m_lstParcel;
    public LinkedList GetParcel() { return m_lstParcel; }
    
    public CommandItem( AStatementExeThread proc, LinkedList parcel) {
        m_processor = proc;
        m_lstParcel = parcel;
        m_strCommandId = "";
    }
}
