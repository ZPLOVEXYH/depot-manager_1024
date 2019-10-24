package cn.samples.depot.common.model.msmq;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(MyPropertyNamingStrategyBase.class)
public class ManifestBase implements Serializable {

    private static final long serialVersionUID = -3004824622398622080L;

    //新舱单表头
//    @Valid
//    @NotNull(message = "[MANIFEST_HEAD_NEW]不能为空")
//    private ManifestHeadNew manifestHeadNew;
//
//    //新舱单提运单
//    @Valid
//    @NotNull(message = "[MANIFEST_LIST_NEW]不能为空")
//    @XStreamImplicit(itemFieldName="manifestListNew")
//    private List<ManifestListNew> manifestListNew;
//
//    //新舱单集装箱信息
//    @Valid
//    @XStreamImplicit(itemFieldName="manifestContaNew")
//    private List<ManifestContaNew> manifestContaNew;
//
//    //新舱单提运单与集装箱关系
//    @Valid
//    @XStreamImplicit(itemFieldName="manifestListContaNew")
//    private List<ManifestListContaNew> manifestListContaNew;
//
//    //新舱单货物信息
//    @Valid
//    @XStreamImplicit(itemFieldName="manifestGoodsNew")
//    private List<ManifestGoodsNew> manifestGoodsNew;
//
//    //新舱单公司信息
//    @Valid
//    @XStreamImplicit(itemFieldName="manifestCopNew")
//    private List<ManifestCopNew> manifestCopNew;

}