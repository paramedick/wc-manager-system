package cn.com.wudskq.service.impl;


import cn.com.wudskq.mapper.PositionMapper;
import cn.com.wudskq.model.Position;
import cn.com.wudskq.model.query.PositionQueryDTO;
import cn.com.wudskq.service.PositionService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenfangchao
 * @title: PositionServiceImpl
 * @projectName wc-manager-system
 * @description: TODO
 * @date 2022/6/28 3:33 AM
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    public List<Position> getPositionList(PositionQueryDTO positionQuery) {
        PageHelper.startPage(positionQuery.getPageNum(),positionQuery.getPageSize());
        return positionMapper.getPositionList(positionQuery);
    }

    @Override
    public Position getPositionDetail(Long id) {
        return positionMapper.getPositionDetail(id);
    }

    @Override
    public void savePosition(Position positionInfo) {
        positionMapper.insert(positionInfo);
    }

    @Override
    public void updatePosition(Position positionInfo) {
        positionMapper.updateById(positionInfo);
    }

    @Override
    public void removePosition(List<Long> ids) {
        positionMapper.removePosition(ids);
    }
}
