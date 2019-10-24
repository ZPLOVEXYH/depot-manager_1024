package cn.samples.depot.web.dto.shipment;

import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.entity.BShipmentGoodsDetail;
import cn.samples.depot.web.entity.BShipmentPlan;

/**
 * 导入excel模型
 */
public class ImportExcelDto implements IExcelModel {

    @ExcelEntity(name = "bShipmentPlan")
    private BShipmentPlan bShipmentPlan;

    @ExcelEntity(name = "bShipmentContainer")
    private BShipmentContainer bShipmentContainer;

    @ExcelEntity(name = "bShipmentGoodsDetail")
    private BShipmentGoodsDetail bShipmentGoodsDetail;

    public BShipmentPlan getbShipmentPlan() {
        return bShipmentPlan;
    }

    public void setbShipmentPlan(BShipmentPlan bShipmentPlan) {
        this.bShipmentPlan = bShipmentPlan;
    }

    public BShipmentContainer getbShipmentContainer() {
        return bShipmentContainer;
    }

    public void setbShipmentContainer(BShipmentContainer bShipmentContainer) {
        this.bShipmentContainer = bShipmentContainer;
    }

    public BShipmentGoodsDetail getbShipmentGoodsDetail() {
        return bShipmentGoodsDetail;
    }

    public void setbShipmentGoodsDetail(BShipmentGoodsDetail bShipmentGoodsDetail) {
        this.bShipmentGoodsDetail = bShipmentGoodsDetail;
    }

    // 自定义一个errorMsg接受下面重写IExcelModel接口的get和setErrorMsg方法。
    private String errorMsg;

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ImportExcelDto() {
    }

    public ImportExcelDto(BShipmentPlan bShipmentPlan, BShipmentContainer bShipmentContainer, BShipmentGoodsDetail bShipmentGoodsDetail, String errorMsg) {
        this.bShipmentPlan = bShipmentPlan;
        this.bShipmentContainer = bShipmentContainer;
        this.bShipmentGoodsDetail = bShipmentGoodsDetail;
        this.errorMsg = errorMsg;
    }
}
