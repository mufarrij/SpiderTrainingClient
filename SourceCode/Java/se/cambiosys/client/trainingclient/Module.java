package se.cambiosys.client.trainingclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.cambiosys.client.framework.AbstractModule;
import se.cambiosys.client.framework.DefaultExceptionHandler;
import se.cambiosys.client.framework.Framework;
import se.cambiosys.client.framework.ModuleInformation;
import se.cambiosys.client.framework.access.AccessToolkit;
import se.cambiosys.client.framework.components.CambioMenu;
import se.cambiosys.client.framework.components.CambioMenuItem;
import se.cambiosys.client.trainingclient.datahandler.ClientDataManager;
import se.cambiosys.client.trainingclient.settings.SettingToolKit;
import se.cambiosys.client.trainingclient.util.I18NToolkit;
import se.cambiosys.client.trainingclient.views.MedicalRecordView;
import se.cambiosys.spider.medicalrecordsmodule.TrainingMedicalNoteData;
import se.cambiosys.spider.StructureService.DateTime;
import se.cambiosys.spider.StructureService.Date;
import se.cambiosys.spider.StructureService.Time;
import se.cambiosys.spider.AccessService.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * module class provide module information and specify menu item and action
 * @author M.Mufarrij
 * @version 1.0 - 3/19/2019
 */
public class Module extends AbstractModule
{
  private ModuleInformation moduleInformation;

  private static final Logger logger = LoggerFactory.getLogger(Module.class);

  public static final String MODULE_NAME = "SpiderClient";

  public Module()
  {
    moduleInformation = new ModuleInformation("Test Client", "moduleId", "This is the description", null);
  }

  @Override public ModuleInformation getModuleInformation()
  {
    return moduleInformation;
  }

  @Override public void initiateModule()
  {
    logger.error("Module  Initiated");
    String name = I18NToolkit.getResourceString("Cosmictraining.client.menu");
    CambioMenu cambioMenu = new CambioMenu(I18NToolkit.getResourceString("Cosmictraining.client.menu"));
    CambioMenuItem menuItemOne = new CambioMenuItem(I18NToolkit.getResourceString("Cosmictraining.client.menu"), "itemId")
    {
      @Override
      public void itemSelected(ActionEvent e)
      {

        JFrame f = new JFrame();


        try
        {
          String msg = ClientDataManager.getData();
          JOptionPane.showMessageDialog(f, msg);
          /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
          Date date = dateFormat.parse("23/04/2007");*/

          DateTime created = new DateTime();
          created.date = new Date((short)1933,(short)10,(short)10);
          created.time = new Time((short)1,(short)12,(short)36);

          DateTime dateTime = new DateTime();
          dateTime.date = new Date((short)2016,(short)12,(short)6);
          dateTime.time = new Time((short)6,(short)6,(short)6);

          /*String createdDate = DateTimeToolkit.toRaw(created);
          String dateTimeString = DateTimeToolkit.toRaw(dateTime);*/

          List<TrainingMedicalNoteData> medicalNoteDataList = ClientDataManager.getDataByKeys(new long[] {29,26,15});

          TrainingMedicalNoteData retrievedData = ClientDataManager.getTraingMedicalNoteData(30);
          retrievedData.setActive(false);
          //long key = retrievedData.versionedData.key;
          //TrainingMedicalNoteData trainingMedicalNoteData = new TrainingMedicalNoteData(key,true,1,dateTime,"patientHealth","stomack knee pain",8,12,13);
          long state = ClientDataManager.setData(retrievedData);
        }
        catch(Exception e1)
        {
          DefaultExceptionHandler.getInstance().handleThrowable(e1);
        }
      }
    };

    CambioMenuItem menuItemTwo = new CambioMenuItem("MedicalRecord", "itemId1"){
      @Override
      public void itemSelected(ActionEvent e)
      {
        boolean readPermission = authorizeReadCurrentUser();
        boolean writePermission = authorizeWriteCurrentUser();
        if(readPermission || writePermission)
        {
          MedicalRecordView.getInstance().setVisible(true);
        }
      }
    };

    cambioMenu.add(menuItemOne);
    cambioMenu.add(menuItemTwo);
    Framework.getInstance().registerMenu(moduleInformation.getName(), cambioMenu);
    SettingToolKit.registerSettings(MODULE_NAME);

  }

  @Override
  public Object getServiceObject()
  {
    return null;
  }

  @Override
  public void login()
  {
    // Logic to be executed when user Login
  }

  @Override
  public void logout()
  {

  }

  public boolean authorizeReadCurrentUser()
  {
    return AccessToolkit.getInstance().accessGranted(new String[]{"se","cambiosys","client","trainingclient","AccessPermission"},"AllowRead",new Dimension[]{},Framework.getInstance().getCurrentUserId());
  }

  public boolean authorizeWriteCurrentUser()
  {
    return AccessToolkit.getInstance().accessGranted(new String[]{"se","cambiosys","client","trainingclient","AccessPermission"},"AllowWrite",new Dimension[]{},Framework.getInstance().getCurrentUserId());
  }
}
