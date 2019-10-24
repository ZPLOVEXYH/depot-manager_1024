package cn.samples.depot.web.controller;

import cn.samples.depot.common.model.CRUDView;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.service.CRUDService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.samples.depot.common.utils.Controllers.ID;
import static cn.samples.depot.common.utils.Controllers.NEW;

@SuppressWarnings(value = {"rawtypes"})
public class CRUDController<service extends CRUDService<T>, T> {

    @Autowired
    private CRUDService<T> service;

    protected Params aggregate(AggregateType type, Params params) {
        return params;
    }

    @GetMapping(ID)
    @ApiOperation("查看指定ID对象")
    @JsonView(CRUDView.Form.class)
    public JsonResult viewT(@PathVariable String id) throws Throwable {
        return JsonResult.data(aggregate(AggregateType.VIEW, Params.param(service.fetch(id))));
    }

    @GetMapping(NEW)
    @ApiOperation("新建")
    @JsonView(CRUDView.Form.class)
    public JsonResult newT() {
        return JsonResult.data(aggregate(AggregateType.NEW, Params.param(service.empty())));
    }

    @PostMapping
    @ApiOperation("保存")
    @JsonView(CRUDView.Form.class)
    public JsonResult saveT(@RequestBody T t) throws Throwable {
        return JsonResult.data(aggregate(AggregateType.SAVE, Params.param(service.saveT(t))));
    }

    @PutMapping(ID)
    @ApiOperation("更新")
    public JsonResult updateT(@PathVariable String id, @RequestBody T t) throws Throwable {
        return JsonResult.data(aggregate(AggregateType.UPDATE, Params.param(service.updateT(id, t))));
    }

    @DeleteMapping(ID)
    @ApiOperation("删除")
    public JsonResult delete(@PathVariable String id) throws Throwable {
        service.deleteT(id);
        return JsonResult.success();
    }

    enum AggregateType {
        QUERY, NEW, SAVE, VIEW, UPDATE
    }

}
