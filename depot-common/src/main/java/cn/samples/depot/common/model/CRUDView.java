package cn.samples.depot.common.model;

import cn.samples.depot.common.utils.JsonResult;

public interface CRUDView extends JsonResult.View {
    interface Base extends CRUDView {
    }

    interface Table extends Base {
    }

    interface Form extends Base {
    }

    interface Detail extends Base {
    }
}
