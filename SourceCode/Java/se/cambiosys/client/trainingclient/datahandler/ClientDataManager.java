package se.cambiosys.client.trainingclient.datahandler;

import se.cambiosys.client.framework.DefaultExceptionHandler;
import se.cambiosys.client.framework.Framework;
import se.cambiosys.spider.StructureService.SpiderException;
import se.cambiosys.spider.medicalrecordsmodule.MedicalRecordManager;
import se.cambiosys.spider.medicalrecordsmodule.MedicalRecordManagerHome;
import se.cambiosys.spider.medicalrecordsmodule.TrainingMedicalNoteData;

import java.rmi.RemoteException;
import java.util.List;

public class ClientDataManager
{

  public static String getData() throws SpiderException
  {

    try
    {
      MedicalRecordManager
      medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);

      return medicalRecordManager.getData();
    }
    catch (RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }
    return "";
  }

  public static long saveData(TrainingMedicalNoteData data) throws SpiderException
  {
    try
    {
      MedicalRecordManager
        medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);
      return medicalRecordManager.createAdditionalInfoFromData(data);

    }catch(RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }

    return -1;
  }

  public static long setData(TrainingMedicalNoteData data) throws SpiderException
  {
    try
    {
      MedicalRecordManager
        medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);
      return medicalRecordManager.setData(data);

    }catch(RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }

    return -1;
  }

  public static long updateData(TrainingMedicalNoteData data,long key) throws SpiderException
  {
    try
    {
      MedicalRecordManager
        medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);
      return medicalRecordManager.updateData(data,key);

    }catch(RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }

    return -1;
  }




  public static TrainingMedicalNoteData getTraingMedicalNoteData(long key) throws SpiderException
  {
    try
    {
      MedicalRecordManager
        medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);
      return  medicalRecordManager.getTrainingMedicalNoteData(key);

    }catch(RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }

    return null;
  }


  public static List<TrainingMedicalNoteData> getDataByKeys(long[] keys) throws SpiderException
  {
    try
    {
      MedicalRecordManager
        medicalRecordManager = (MedicalRecordManager) Framework.getInstance().getEJBObjectFromSpider("medicalrecordsmodule/MedicalRecordManager", MedicalRecordManagerHome.class);
      return  medicalRecordManager.getDataByKeys(keys);

    }catch(RemoteException e)
    {
      DefaultExceptionHandler.getInstance().handleThrowable(e);
    }

    return null;
  }

}
