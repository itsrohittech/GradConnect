"""gradconnect URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from connectapp import views
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('',views.index),
    path('login/',views.loginn),
    path('admin_dashboard/',views.admin_dashboard),
    path('user_dashboard/',views.user_dashboard),
    path('logout/',views.logout),
    path('user_signup/',views.user_signup),
    path('admin_signup/',views.admin_signup),
    path('admin_view_request/',views.admin_view_request),
    path('admin_handle_request/<int:reg_id>/<str:action>/', views.admin_handle_request, name='admin_handle_request'),
    path('edit_profile/',views.edit_profile),    
    path('add_or_update_startup/',views.add_or_update_startup),    
    path('add_or_update_marital_status/',views.add_or_update_marital_status),    
    path('add_or_update_business/',views.add_or_update_business),    
    path('add_or_update_higher_studies/',views.add_or_update_higher_studies),    
    path('add_or_update_other/',views.add_or_update_other),    
    path('admin_user_list/',views.admin_user_list),    
    path('admin_view_user_detials/<id>/',views.admin_view_user_detials),    
    path('admin_view_startup/<id>/',views.admin_view_startup),    
    path('admin_view_marital_status/<id>/',views.admin_view_marital_status),    
    path('admin_view_business/<id>/',views.admin_view_business),    
    path('admin_view_higher_studies/<id>/',views.admin_view_higher_studies),    
    path('admin_view_other/<id>/',views.admin_view_other),    
    path('user_msg_admin',views.user_msg_admin),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    #path('college_signup',views.college_signup),    
    
]
