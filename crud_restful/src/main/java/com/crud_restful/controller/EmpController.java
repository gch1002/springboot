package com.crud_restful.controller;

import com.crud_restful.annotation.LoggerManage;
import com.crud_restful.domain.Emp;
import com.crud_restful.exception.ServiceEnumType;
import com.crud_restful.exception.ServiceException;
import com.crud_restful.service.EmpService;
import com.crud_restful.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/emp")
public class EmpController {
    @Autowired
    private EmpService empService;

    @LoggerManage(logDescription = "根据ID查询")
    @GetMapping("/{id}")
    public RespBean getUser(@PathVariable("id") Integer id) throws Exception {
        try {
            Emp emp = empService.getEmpById(id);
            return RespBean.success(ServiceEnumType.GET_SUCCESS.getCode(), ServiceEnumType.GET_SUCCESS.getMsg(), emp);
        } catch (Exception e) {
            throw new ServiceException(ServiceEnumType.GET_FAIL.getCode(), ServiceEnumType.GET_FAIL.getMsg());
        }
    }

    @GetMapping("/page")
    public RespBean getUserByPage(@RequestParam Integer page, @RequestParam(defaultValue = "3") Integer size) throws Exception {
        try {
            if (page < 1) {
                page = 1;
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Emp> empByPage = empService.getEmpByPage(pageable);
            return RespBean.success(ServiceEnumType.GET_SUCCESS.getCode(), ServiceEnumType.GET_SUCCESS.getMsg(), empByPage);
        } catch (Exception e) {
            throw new ServiceException(ServiceEnumType.GET_FAIL.getCode(), ServiceEnumType.GET_FAIL.getMsg());
        }
    }

    @PutMapping("/update")
    public RespBean updateUser(@RequestBody Emp emp) throws Exception {
        try {
            Emp emp1 = empService.updateEmp(emp);
            return RespBean.success(ServiceEnumType.UPDATE_SUCCESS.getCode(), ServiceEnumType.UPDATE_SUCCESS.getMsg(), emp1);
        } catch (Exception e) {
            throw new ServiceException(ServiceEnumType.UPDATE_FAIL.getCode(), ServiceEnumType.UPDATE_FAIL.getMsg());
        }
    }

    @PostMapping("/add")
    public RespBean addUser(@RequestBody @Valid Emp emp) throws Exception {
        try {
            // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
           /* for (ObjectError error : bindingResult.getAllErrors()) {
                return RespBean.fail(0,error.getDefaultMessage());

            }*/
            Emp emp1 = empService.saveEmp(emp);
            return RespBean.success(ServiceEnumType.INSERT_SUCCESS.getCode(), ServiceEnumType.INSERT_SUCCESS.getMsg(), emp1);
        } catch (Exception e) {
            throw new ServiceException(ServiceEnumType.INSERT_FAIL.getCode(), ServiceEnumType.INSERT_FAIL.getMsg());
        }
    }

    @DeleteMapping("/delete/{id}")
    public RespBean deleteUser(@PathVariable("id") Integer id) throws Exception {
        try {
            empService.deleteEmp(id);
            return RespBean.success(ServiceEnumType.DELETE_SUCCESS.getCode(), ServiceEnumType.DELETE_SUCCESS.getMsg());
        } catch (Exception e) {
            throw new ServiceException(ServiceEnumType.DELETE_FAIL.getCode(), ServiceEnumType.DELETE_FAIL.getMsg());
        }
    }

    @PostMapping("/test")
    public RespBean add() throws Exception {

        throw new ServiceException(00000, "11111111");
    }
}
