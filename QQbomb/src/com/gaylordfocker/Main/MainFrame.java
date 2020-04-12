package com.gaylordfocker.Main;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.gaylordfocker.dialog.QQDialog;
import com.gaylordfocker.dialog.SettingDialogFactory;

/**
 * 没啥好写的呀
 * @author GaylordFocker
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame{

	public final int DEFAULT_WIDTH = 300,DEFAULT_HEIGHT = 400;
	
	private JPanel centerPn;
	
	private SettingDialogFactory factory;
	
	private JButton addQQDialogBtn,helpBtn;
	
	public final String version = "1.0";
	
	private ArrayList<QQDialog> dialogs = new ArrayList<QQDialog>();
	
	public MainFrame() {
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("QQ轰炸鸡"+version);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("config/dalao.png").getImage());
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop(true);//防止无法停止进程
		setLayout(null);
		init();
	}
	
	public void init() {
		
		JLabel lb = new JLabel("|   对象   |    内容    |   间隔     |   状态    |        操作           |");
		lb.setBorder(BorderFactory.createLoweredBevelBorder());
		lb.setBounds(0, 0, DEFAULT_WIDTH, 30);
		add(lb);
		
		centerPn = new JPanel();
		centerPn.setLayout(null);
		centerPn.setBounds(0, 30, DEFAULT_WIDTH, 300);
		add(centerPn);
		
		addQQDialogBtn = new JButton("+新建任务");
		addQQDialogBtn.setBounds(90, 334, 100, 20);
		addQQDialogBtn.addActionListener((e)->{
			if(dialogs.size()==10) {
				JOptionPane.showMessageDialog(this, "进程太多了啦（＞人＜；）");
			}
			factory = new SettingDialogFactory(this);//省事，反正有GC
			QQDialog d = factory.createSettingDialog(dialogs.size());
			if(d==null) {
				return;
			}
			d.setRemoveAction((a)->{
				
				if(JOptionPane.showConfirmDialog(this, "确认删除此对话？", "确认", JOptionPane.YES_NO_OPTION)
						==JOptionPane.YES_OPTION) {
					new Thread(()->{
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						centerPn.remove(d);
						for(int i = d.getSequence();i<dialogs.size();i++) {
							dialogs.get(i).turnUp();
						}
						repaint();
						dialogs.remove(d);
					}).start();
				}
				
			});
			if(dialogs.contains(d)) {
				JOptionPane.showMessageDialog(this, "该对象已经存在!", "错误", JOptionPane.ERROR_MESSAGE);
			}else {
				centerPn.add(d);
				repaint();
				dialogs.add(d);
			}
		});
		addQQDialogBtn.setBorder(BorderFactory.createRaisedBevelBorder());
		add(addQQDialogBtn);
		
		helpBtn = new JButton(new ImageIcon(
				new ImageIcon("config/help.jpg").getImage()
				.getScaledInstance(20, 20, Image.SCALE_FAST)));
		helpBtn.setBounds(220, 334, 20, 20);
		helpBtn.setToolTipText("帮助");
		helpBtn.addActionListener((e)->{
			JOptionPane.showMessageDialog(this, "有啥好说的呀？要注意的就使用的时候把聊天窗口分开就行了。",
					"帮助                     © 2020 GaylordFocker 版权所有", JOptionPane.INFORMATION_MESSAGE);
		});
		add(helpBtn);
	}
	
	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}

}