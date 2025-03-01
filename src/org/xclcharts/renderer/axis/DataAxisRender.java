/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version v0.1
 */

package org.xclcharts.renderer.axis;


/**
 * @ClassName DataAxis
 * @Description 数据轴(Data Axis)绘制类，负责具体的绘制
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  * MODIFIED    YYYY-MM-DD   REASON
 */

import org.xclcharts.renderer.IRender;

import android.graphics.Canvas;

public class DataAxisRender extends DataAxis implements IRender{


	public DataAxisRender()
	{
		super();
	}
		
	/**
	 * 返回轴值的范围(即最大-最小值).
	 * @return 轴值范围
	 */
	public double getAxisRange()
	{
		return Math.abs(getAxisMax() - getAxisMin());		
	}
	
	
	/**
	 * 数据轴值范围(最大与最小之间的范围)  /  传的的步长  = 显示的Tick总数
	 * @return 显示的刻度标记总数
	 */
	public double getAixTickCount()
	{
		double tickCount = Math.ceil( getAxisRange() / getAxisSteps() ) ;
		
		return tickCount;
	}
		
	/*
	 * 绘制横向刻度标记
	 */
	public 	void renderAxisHorizontalTick(float centerX,float centerY,String text)
	{		
		if(getVisible())
			renderHorizontalTick(centerX,centerY,text);
	}

	/**
	 * 绘制竖向刻度标记
	 * @param centerX
	 * @param centerY
	 * @param text
	 */
	public void renderAxisVerticalTick(float centerX,float centerY,String text)
	{
		if(getVisible())
			renderVerticalTick(centerX,centerY,text);
	}
	
	
	/**
	 * 绘制轴
	 * @param startX
	 * @param startY
	 * @param stopX
	 * @param stopY
	 */
	public void renderAxis(float startX,float startY,float stopX,float stopY)
	{
		if(getVisible() && getAxisLineVisible())
			mCanvas.drawLine(startX, startY, stopX, stopY, this.getAxisPaint());
	}	
	
	@Override
	public void setCanvas(Canvas canvas) {
		// TODO Auto-generated method stub
		mCanvas = canvas;
	}

	@Override
	public boolean render() {
		// TODO Auto-generated method stub
		if(false == getVisible())return true;
		
		return true;
	}

}
