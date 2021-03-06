package opengl.control;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import opengl.MeuOpenGL;

/**
 *
 * @author Leonardo Villeth
 */
public class MapaTeclado extends InputMap {
        
    ComponentInputMap  inputMap;
    ActionMap actionMap;

    public MapaTeclado(JComponent component, Camera cam, MeuOpenGL render) {
        inputMap = new ComponentInputMap(component);
        actionMap = new ActionMap();
        
        // rotaciona a camera para direita
        Action rotCamDir = new AbstractAction("rDir") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                
                cam.rotacionarY(1.5f);                
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),"rDir");        
        actionMap.put("rDir", rotCamDir);
        
        // rotaciona a camera para esquerda
        Action rotCamEsq = new AbstractAction("rEsq") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                
                cam.rotacionarY(-1.5f);                
            }
        };                
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),"rEsq");        
        actionMap.put("rEsq", rotCamEsq);
        
        // move a para frente 
        Action tFrente = new AbstractAction("frente") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                               
                cam.moveFrente();                
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false),"frente");        
        actionMap.put("frente", tFrente);
        
        // move a para tras     
        Action tTras = new AbstractAction("tras") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                cam.moveTras();                
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false),"tras");        
        actionMap.put("tras", tTras);
        
        // move a para esquerda     
        Action tEsq = new AbstractAction("esq") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                cam.moveEsq();                
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false),"esq");        
        actionMap.put("esq", tEsq);
        
        // move a para direita     
        Action tDir = new AbstractAction("dir") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                cam.moveDir();                
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false),"dir");        
        actionMap.put("dir", tDir);
        
        Action disparaFogos = new AbstractAction("dispf") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                render.disparaFogos();
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0, false),"dispf");        
        actionMap.put("dispf", disparaFogos);
        
        
        Action up = new AbstractAction("up") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                cam.rotacionarX(1.0f);
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false),"up");        
        actionMap.put("up", up);
        
        Action down = new AbstractAction("down") {
            @Override
            public void actionPerformed(ActionEvent e) {                                                                           
                cam.rotacionarX(-1.0f);
            }
        };        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),"down");        
        actionMap.put("down", down);
    }
    
    public InputMap getInputMap(){
        return inputMap;
    }
    
    public ActionMap getActionMap(){
        return actionMap;
    }
    
    
    
}
