package cn.samples.depot.web.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.samples.depot.web.dto.shipment.ImportExcelDto;

import java.util.ArrayList;
import java.util.List;

public class ImportExcelPlanFailed extends ImportExcelDto {

    @Excel(name = "错误信息")
    private String errorMsg;

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static ImportExcelPlanFailed member2ImportExcelPlanFailed(ImportExcelDto excelDto) {
        ImportExcelPlanFailed failed = new ImportExcelPlanFailed();
        failed.setErrorMsg(excelDto.getErrorMsg());
        failed.setbShipmentPlan(excelDto.getbShipmentPlan());
        failed.setbShipmentContainer(excelDto.getbShipmentContainer());
        failed.setbShipmentGoodsDetail(excelDto.getbShipmentGoodsDetail());
        return failed;
    }

    public static List<ImportExcelPlanFailed> members2ImportExcelPlanFaileds(List<ImportExcelDto> excelDtoList) {
        List<ImportExcelPlanFailed> list = new ArrayList<>();
        for (ImportExcelDto excelDto : excelDtoList) {
            list.add(member2ImportExcelPlanFailed(excelDto));
        }
        return list;
    }
}
