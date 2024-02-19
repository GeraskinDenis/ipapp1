package EntityTypeRule;

import java.util.*;
import java.math.*;
import java.util.concurrent.*;
import java.sql.Timestamp;
import org.joda.time.*;
import ru.ip.server.logging.Log;
import ru.ip.server.logic.message.MessageType;
import ru.ip.server.entity.EntityDTO;
import ru.ip.server.utils.*;
import ru.ip.server.utils.http.*;
import ru.ip.server.exception.*;
import ru.ip.server.rest.client.EntityRestClient;
import ru.ip.server.database.sql.SQLUtils;
import ru.ip.server.database.sql.SimpleQuery;
import ru.ip.server.database.sql.ScrollQuery;
import ru.ip.server.threadpool.*;
import ru.ip.server.integration.v2.*;
import ru.ip.server.integration.v2.elements.*;
import ru.ip.server.office.loop.ExcelDataHolder;
import ru.ip.server.measureunit.*;
import ru.ip.server.module.*;
import ru.ip.server.module.cms.*;
import ru.ip.server.module.cms.model.*;
import ru.ip.server.module.itsm.*;
import ru.ip.server.module.itsm.model.*;
import ru.ip.server.module.planning.*;
import ru.ip.server.integration.mdm.*;
import ru.ip.server.integration.mdm.model.*;

/**
DisplayName: Параметры типа КЕ (itsm)
Fields:
 - calculation (Вычисление (paramValue)) - TEXT
 - calculationtype (Способ вычисления) - STRING
 - ciparamsid (ID) - INTEGER
 - citype (Тип КЕ) - INTEGER
 - createdbyid (Кем создано) - INTEGER
 - createdtime (Дата создания) - DATETIME
 - defaultvalue (Значение по умолчанию) - STRING
 - displayname (Наименование) - STRING
 - fieldtype (Тип данных) - STRING
 - isactive (Активно) - BOOLEAN
 - param_type (Тип параметра) - STRING
 - paramname (Имя параметра) - STRING
 - shortname (Внутреннее имя) - STRING
 - skipvisualization (Не показывать при визуализации) - BOOLEAN
 - sortorder (Порядок вычисления) - INTEGER
 - unitofmeasure (Единицы измерения) - INTEGER
 - updatedbyid (Кем обновлено) - INTEGER
 - updatedtime (Дата обновления) - DATETIME
 - value (Нормативное значение (Удалить?)) - STRING
 - value_format (Формат значения) - INTEGER
*/

public class citypeparams_Rules {

	// Необходимые для корректной работы компилятора переменные
	private static final Log log = Log.getLogger("ClassLibraryLogger");
	private static Map<String, Object> searchFields = new HashMap<>();
	private static Map<String, Object> updateFields = new HashMap<>();
	private static List<EntityDTO> recordList = new ArrayList<>();

	public static void afterUpdate(EntityDTO record, EntityDTO oldrecord) throws Exception {

		//region Generate data
		//rule <sn>53b3d146-3119-dbc6-b894-8fcc7f038707</sn>
		if (/*if*/true/*if*/) {
			//<body>
			//-----Шпалы
			//Lib.GenerateDataUtils.deleteSleepersData();
			//Lib.GenerateDataUtils.generateSleepersSH1Data();
			//Lib.GenerateDataUtils.generateSleepersWoodData();
			//Lib.GenerateDataUtils.generateSleepersData();
			
			//-----Рельсы
			//Lib.GenerateDataUtils.deleteRailsData();
			//Lib.GenerateDataUtils.generateRailsData();
			
			//-----РШР
			//Lib.GenerateDataUtils.deleteRsgridData();
			//Lib.GenerateDataUtils.generateRsgridData();
			
			//-----Изостыки
			//Lib.GenerateDataUtils.deleteIsojointData();
			//Lib.GenerateDataUtils.generateIsojointData();
			
			//-----Скрепления
			//Lib.GenerateDataUtils.deleteClipsData();
			//Lib.GenerateDataUtils.generateClipsData();
			
			//-----Стрелочные переводы
			//Lib.GenerateDataUtils.deleteTurnoutData();
			//Lib.GenerateDataUtils.generateTurnoutData();
			
			//-----Щебень
			//Lib.GenerateDataUtils.deleteBallastData();
			//Lib.GenerateDataUtils.generateBallastData();
			
			//-----Накладки
			//Lib.GenerateDataUtils.deleteStrapsData();
			//Lib.GenerateDataUtils.generateStrapsData();
			
			//-----Геотекстиль
			//Lib.GenerateDataUtils.deleteGeotextileData();
			//Lib.GenerateDataUtils.generateGeotextileData();
			
			//-----Пенополистирол
			//Lib.GenerateDataUtils.deleteStyrofoamData();
			//Lib.GenerateDataUtils.generateStyrofoamData();
			
			//-----Георешетка
			//Lib.GenerateDataUtils.deleteGeogridData();
			//Lib.GenerateDataUtils.generateGeogridData();
			
			//-----Геосетка
			//Lib.GenerateDataUtils.deleteGeonetData();
			//Lib.GenerateDataUtils.generateGeonetData();
			
			//-----Перегоны
			//Lib.GenerateDataUtils.deleteRailwayTrackData();
			
			//-----Пути
			//Lib.GenerateDataUtils.deleteTrackNumData();
			
			//-----Платформы
			//Lib.GenerateDataUtils.deletePlatformData();
			
			//-----ММ
			//Lib.GenerateDataUtils.deleteMechAndToolsData();
			
			//-----Вагоны
			//Lib.GenerateDataUtils.deleteRailcarData();
			
			//-----ГПМ
			//Lib.GenerateDataUtils.deleteLiftingMechData();
			
			//-----АТТ
			//Lib.GenerateDataUtils.deleteAttTechData();
			//Lib.GenerateDataUtils.generateDeptAttTechNeedData();
			//Lib.GenerateDataUtils.deleteDeptAttTechNeedData();
			
			//-----Здания и сооружения
			//Lib.GenerateDataUtils.deleteBuildingData();
			
			//-----Виды ремонта ПМС (паспорт)
			//Lib.GenerateDataUtils.deleteDeptRepairTypeData();
			//Lib.GenerateDataUtils.generateDeptRepairTypeData();
			
			//-----Специализации план/факт ПМС (паспорт)
			//Lib.GenerateDataUtils.deleteDeptSpecializationData();
			//Lib.GenerateDataUtils.generateDeptSpecializationData();
			
			//-----АТТ потребность/факт ПМС (паспорт)
			//Lib.GenerateDataUtils.generateAttTechCountData();
			
			//-----Вагоны потребность/факт ПМС (паспорт)
			//Lib.GenerateDataUtils.deleteRailcarCountData();
			//Lib.GenerateDataUtils.generateRailcarCountData();
			
			//-----Аварийный инструмент план/факт ПМС (паспорт)
			//Lib.GenerateDataUtils.generateCrashRecoveryToolsData();
			//Lib.GenerateDataUtils.generateCrashRecoveryTools19Data();
			
			
			//-----Ручной инструмент план/факт ПМС (паспорт)
			//Lib.GenerateDataUtils.generateHandInstrumentData();
			//Lib.GenerateDataUtils.deleteHandInstrumentData();
			
			//-----Энергетические ресурсы ПМС (паспорт)
			//Lib.GenerateDataUtils.generateEnergyResourcestData();
			
			//-----Показатели учета приборов ТЭР ПМС (паспорт)
			//Lib.GenerateDataUtils.generateEnergyDevicesData();
			
			//-----Приборы ТЭР ПМС (паспорт)
			//Lib.GenerateDataUtils.deleteEnergyMeteringDevicesData();
			
			//-----Численность по штатному расписанию (паспорт)
			//Lib.GenerateDataUtils.generateDeptUsergroupCountData();
			//Lib.GenerateDataUtils.deleteDeptUsergroupCountData();
			
			//-----Оборудование (паспорт)
			//Lib.GenerateDataUtils.deleteEquipmentData();
			
			//-----Запас рельсов inv (паспорт)
			//Lib.GenerateDataUtils.
			
			// Обновление данных Автомобили
			//Lib.GenerateDataUtils.updateAutomobileData();
			
			//Обновление ММ, как у ОПМС-19
			//Lib.GenerateDataUtils.generateMechTools19Data();
			//Lib.GenerateDataUtils.deleteMechAndToolsData();
			
			//Обновление таблицы citype
			//Lib.GenerateDataUtils.updateCitypeData();
			
			//Обновление таблицы building
			//Lib.GenerateDataUtils.updateBuildingData();
			
			//Обновление таблицы request
			//Lib.GenerateDataUtils.updateRequestData();
			
			//Обновление таблицы user
			//Lib.GenerateDataUtils.updateUserData();
			
			//Обновление таблицы ПМ
			//Lib.GenerateDataUtils.updateTrackMachinesData();
			
			//Обновление таблицы track_constructions
			//Lib.GenerateDataUtils.updateTrackConstructionsData();
			
			//Lib.GenerateDataUtils.updateLaborMaintenanceData();
			//Lib.GenerateDataUtils.deleteLaborMaintenanceData();
			//Lib.GenerateDataUtils.updateTaskLaborMaintenanceData();
			//Lib.GenerateDataUtils.updateAddMachineData();
			
			//Lib.GenerateDataUtils.updateTaskReqMaintData();
			
			//Lib.GenerateDataUtils.generateReducedKmOperations();
			//Lib.GenerateDataUtils.deleteReducedKmOperations();
			
			//Lib.GenerateDataUtils.updateResourceTitleData();
			
			//-----Потребность ПМ
			//Lib.GenerateDataUtils.generateDeptTMNeedData();
			
			/**Генерация данных Паспорт ПМС */ 
			
			//Обновление типа и рабочей группы в оргстурктуре
			//Lib.GenerateDataUtils.updateOrgunitData();
			
			/*
			//-----Виды ремонта ПМС (паспорт)
			Lib.GenerateDataUtils.generateDeptRepairTypeData();
			log.info("Загружено generateDeptRepairTypeData");
			
			//-----Специализации план/факт ПМС (паспорт)
			Lib.GenerateDataUtils.generateDeptSpecializationData();
			log.info("Загружено generateDeptSpecializationData");
			
			//-----Аварийный инструмент план/факт ПМС (паспорт)
			Lib.GenerateDataUtils.generateCrashRecoveryTools19Data();
			log.info("Загружено generateCrashRecoveryTools19Data");
			
			//-----Ручной инструмент план/факт ПМС (паспорт)
			Lib.GenerateDataUtils.generateHandInstrumentData();
			log.info("Загружено generateHandInstrumentData");
			
			//-----Энергетические ресурсы ПМС (паспорт)
			Lib.GenerateDataUtils.generateEnergyResourcestData();
			log.info("Загружено generateEnergyResourcestData");
			
			//-----Показатели учета приборов ТЭР ПМС (паспорт)
			Lib.GenerateDataUtils.generateEnergyDevicesData();
			log.info("Загружено generateEnergyDevicesData");
			
			//-----Численность по штатному расписанию (паспорт)
			Lib.GenerateDataUtils.generateDeptUsergroupCountData();
			log.info("Загружено generateDeptUsergroupCountData");
			
			//----Обновление ММ, как у ОПМС-19
			Lib.GenerateDataUtils.generateMechTools19Data();
			log.info("Загружено generateMechTools19Data");
			
			//-----Потребность ДСТ
			Lib.GenerateDataUtils.generateDeptAttTechNeedData();
			log.info("Загружено generateDeptAttTechNeedData");
			
			//-----Потребность ПМ
			Lib.GenerateDataUtils.generateDeptTMNeedData();
			log.info("Загружено generateDeptTMNeedData");
			*/
			
			//Lib.GenerateDataUtils.updateTechOperationData();
			
			//Lib.GenerateDataUtils.deleteReqData();
			
			//ContextUtils.addMessage("user dept="+Lib.RorgunitUtils.getUserDept(),"WARNING"); 
			//ContextUtils.addMessage("user dept="+Lib.RorgunitUtils.getUserBranch(),"WARNING");
			//ContextUtils.addMessage("user ou_railway="+Lib.RorgunitUtils.getUserOrgunitRailway(),"WARNING"); 
			
			//ContextUtils.addMessage("cdimid="+Lib.RorgunitUtils.getCDIMId(),"WARNING");
			
			//ContextUtils.addMessage("in cdim="+Lib.RorgunitUtils.checkOrgunitIsInCDIM(12237),"WARNING");
			//ContextUtils.addMessage("in cdrp="+Lib.RorgunitUtils.checkOrgunitIsInCDRP(12237),"WARNING");
			
			//Lib.DeleteUtils.DeleteServices("serviceid in (78, 98, 99, 100, 109, 110, 111)");
			/*
			Date now = new Date();
			String query="isactive=1 and fired_date<=+"+now;
			ContextUtils.addMessage("query="+query,"WARNING");*/
			
			//Lib.GenerateDataUtils.updateEspulSPSSerieData();
			
			//ContextUtils.addMessage("InCDRP="+Lib.RorgunitUtils.checkOrgunitIsInCDRP(1));
			//ContextUtils.addMessage("InCDIM="+Lib.RorgunitUtils.checkOrgunitIsInCDIM(1));
			
			//Lib.GenerateDataUtils.deleteCitypeData();
			
			//Lib.DeleteUtils.deleteRequestTechnologys("request_templateid=6");
			
			//ContextUtils.addMessage("UserOrgunitRailway="+Lib.RorgunitUtils.getUserOrgunitRailway(),"WARNING");
			
			//Lib.GenerateDataUtils.updateAnyObjectData("techtask_template","1=1");
			//Lib.GenerateDataUtils.updateAnyObjectData("rsstype_repairtype","1=1");
			//Lib.GenerateDataUtils.updateAnyObjectData("rsstype_operation_material","1=1");
			
			//Lib.GenerateDataUtils.updateAnyObjectData("mech_and_tools","1=1");
			
			//Lib.WordTemplUtils.getCitypeRtype(45);
			//ContextUtils.addMessage("rtype="+Lib.WordTemplUtils.getCitypeRtype(1410),"WARNING");
			
			//Lib.GenerateDataUtils.generateRatio();
			//Lib.GenerateDataUtils.deleteRatioData();
			//Lib.GenerateDataUtils.copyRatioForNewTaskTempalate(236,636);
			
			//ContextUtils.addMessage("Тестовое сообщение","INFO");
			//ContextUtils.addMessage("Дорога ="+Lib.RorgunitUtils.getUserOrgunitRailway());
			
			//EntityDTO reqtech=QueryUtils.getRecordById("request_template",61);
			//Lib.DeleteUtils.deleteRequestTechnologyInfo(reqtech);
			
			//Lib.GenerateDataUtils.generateExtdeptEconomicParamsData();
			//Lib.GenerateDataUtils.generateExtdeptCoreRepairParamsData();
			//Lib.GenerateDataUtils.generateExtdeptCoreWorkParamsData();
			//Lib.GenerateDataUtils.generateDeptUsergroupCountData();
			//Lib.GenerateDataUtils.deleteAnyObjectData("dept_usergroup_count","CREATEDTIME>='2020-10-28 00:00:00'");
			
			//EntityDTO wp = new EntityDTO("working_period",27263);
			//EntityDTO taskTempl = new EntityDTO("task_template",306);
			//Lib.WorkingPeriodTemplate.createWPOrgTasks(4,wp);
			
			EntityDTO service = new EntityDTO("service",44);
			//Lib.TransportationUtils.createTranspTasks(service, "rsgrid_transport", "rsgrid");
			//Lib.TransportationUtils.createTranspTasks(service, "weldrails_transport", "weldrails_work");
			//Lib.TransportationUtils.createTranspTasks(service, "turnout_transport", "turnout_assembly");
			
			//Boolean taskRequired=isTaskRequired(4,wp, taskTempl);
			//ContextUtils.addMessage("taskRequired ="+Lib.WorkingPeriodTemplate.isTaskRequired(4,wp, taskTempl));
			/*
			DateTime defaultValue = null; 
			int year = new DateTime().getYear(); 
			defaultValue = new DateTime(year, 1, 1, 0, 0, 0, 0);
			ContextUtils.addMessage("defaultValue="+defaultValue);
			ContextUtils.addMessage("year="+year); 
			*/
			
			//List <EntityDTO> objList = QueryUtils.getRecordList("citype","parent_citype in (select c.citypeid from fkv39o1z.v_citype c where c.parent_citype=1899)");
			//	List <EntityDTO> objList = QueryUtils.getRecordList("tools","toolsid between 9999 and 19998");
			/*List <EntityDTO> objList = QueryUtils.getRecordList("norm_rails_tons","CREATEDTIME>='2021-01-14 00:00:00'");
				for (EntityDTO obj:objList){
				    obj.doDelete();
			    }
			    ContextUtils.addMessage("objList="+objList.size(),"WARNING"); */
			/*EntityDTO wp = new EntityDTO("working_period",27263);
			if (wp.get("request_template")!=null){
			    if (CommonUtils.isSame(wp.getAsString("request_template->request_category"),"station_technology"))
			      //  Lib.TaskTemplateUtils.createTurnoutReqTasks(wp.getAsInteger("request_template"),wp);
			        Lib.WorkingPeriodTemplate.createTechTasksFromTechnology(wp.getAsInteger("request_template"),wp);
			} */
			
			//Lib.TransportationUtils.createTranspTasks(service, "turnout_transport", "turnout_assembly");
			
			
			//Lib.GenerateDataUtils.moveEspulToCitype();
			
			//Lib.GenerateDataUtils.unbindRequestFromSevice("service in (153,154)");
			//Lib.DeleteUtils.DeleteServices("serviceid in (132,166)");
			
			//Lib.DeleteUtils.DeleteTasksByQuery("request=18558");
			//EntityDTO task=QueryUtils.getRecordById("task",214705);
			//ContextUtils.addMessage("service="+task.get("service")); 
			/*List <EntityDTO> taskList=QueryUtils.getRecordList("task","service=129");
			for (EntityDTO task:taskList)
			   ContextUtils.addMessage("task="+task.getKeyValue()); 
			ContextUtils.addMessage("taskList="+taskList.size()); */
			/*
			List<EntityDTO> objList = QueryUtils.getRecordList("request","service in (132,166)"); //displayname is null
			    ContextUtils.addMessage("objList="+objList.size());
			        for (EntityDTO obj:objList){
			            obj.set("service",null);
			            obj.set("workflowstepid",369);
			            obj.set("workflowstepname","draft");
			        	obj.doUpdate();
			        }
			 */       
			//Lib.GenerateDataUtils.fillFields();
			
			//Lib.DRPServiceUtils.createYearDRPServices(2021);
			
			//Lib.GenerateDataUtils.updateAnyObjectData("user","isactive=1 and fired_date<=now()");
			
			//int cnt=QueryUtils.getCount("service","railway=58");
			// ContextUtils.addMessage("cnt="+cnt);
			 
			//Lib.GenerateDataUtils.updateAnyObjectData("service","year=2021");
			//Lib.GenerateDataUtils.updateRequestData();
			//EntityDTO drp=QueryUtils.getRecordById("r_orgunit", 6369); //Горьковская ДРП
			//Lib.DRPServiceUtils.createDRPService(drp, 2021);
			//</body>
		}
		//endregion

	}

}
