package com.stewart.building.mbg.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stewart.building.common.renum.SuccessEnum;
import com.stewart.building.mbg.pojo.Experiment;
import com.stewart.building.mbg.mapper.ExperimentMapper;
import com.stewart.building.mbg.service.IExperimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stewart.building.param.experiment.AddExperimentParam;
import com.stewart.building.param.experiment.GetAllExperimentParam;
import com.stewart.building.param.experiment.UpdateExperimentParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Stewart
 * @since 2022-01-06
 */
@Service
public class ExperimentServiceImpl extends ServiceImpl<ExperimentMapper, Experiment> implements IExperimentService {
    private static final Logger logger = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private ExperimentMapper experimentMapper;

    /**
     * 分页/模糊 查询所有的实验
     *
     * @param getAllExperiment
     * @return
     */
    @Override
    public Page<Experiment> getAll(GetAllExperimentParam getAllExperiment) {
        Page<Experiment> page = new Page<>(getAllExperiment.getCurrent(), getAllExperiment.getSize());
        return experimentMapper.selectPage(page, new QueryWrapper<Experiment>().like("name", getAllExperiment.getName()));

    }

    /**
     * 添加实验
     * @param addExperimentParam
     * @return
     */
    @Override
    public int addExperiment(AddExperimentParam addExperimentParam) {
        Experiment experiment = new Experiment();
        BeanUtils.copyProperties(addExperimentParam,experiment);
        experiment.setCreateTime(String.valueOf(DateUtil.currentSeconds()));
        experiment.setNumber("实验"+ RandomUtil.randomInt(1, 10000));
        return experimentMapper.insert(experiment);
    }

    /**
     * 修改实验
     * @param updateExperimentParam
     * @return
     */
    @Override
    public int updateExperiment(UpdateExperimentParam updateExperimentParam) {
        //判断该实验是否存在
        Experiment flag = experimentMapper.selectById(updateExperimentParam.getId());
        if (flag==null){
            return SuccessEnum.ID_NOT_EXIST.ordinal();
        }
        Experiment experiment = new Experiment();
        BeanUtils.copyProperties(updateExperimentParam,experiment);
        experiment.setUpdateTime(String.valueOf(DateUtil.currentSeconds()));
        return experimentMapper.updateById(experiment);
    }

    /**
     * 根据实验id删除实验
     * @param id
     * @return
     */
    @Override
    public int deleteLabById(Integer id) {
        Experiment experiment = experimentMapper.selectById(id);
        if(experiment==null){
            return SuccessEnum.ID_NOT_EXIST.ordinal();
        }
        return experimentMapper.deleteById(id);
    }

    /**
     * 根据id查询实验信息
     * @param id
     * @return
     */
    @Override
    public Experiment getExperimentById(Integer id) {
        return experimentMapper.selectById(id);
    }
}
