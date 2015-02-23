/*
	@license Angular Treeview version 0.1.6
	ⓒ 2013 AHN JAE-HA http://github.com/eu81273/angular.treeview
	License: MIT


	[TREE attribute]
	angular-treeview: the treeview directive
	tree-id : each tree's unique id.
	tree-model : the tree model on $scope.
	node-id : each node's id
	node-label : each node's label
	node-children: each node's children

	<div
		data-angular-treeview="true"
		data-tree-id="tree"
		data-tree-model="roleList"
		data-node-id="roleId"
		data-node-label="roleName"
		data-node-children="children" >
	</div>
*/

(function ( angular ) {
	'use strict';

	angular.module( 'angularTreeview', [] ).directive( 'treeModel', ['$compile', function( $compile ) {
		return {
			restrict: 'A',
			link: function ( scope, element, attrs ) {
				//tree id
				var treeId = attrs.treeId;
			
				//tree model
				var treeModel = attrs.treeModel;

				//node id
				var nodeId = attrs.nodeId || 'id';

				//node label
				var nodeLabel = attrs.nodeLabel || 'label';

				//children
				var nodeChildren = attrs.nodeChildren || 'children';

	  			//server menu
				var menuServer = '<ul id="contextMenuServer" class="dropdown-menu">'+
					'<li><a ng-click="saveServer()">Save server</a></li>'+
				'<li><a ng-click="link()">Link</a></li>'+
				'<li><a ng-click="delete()">Delete</a></li>'+
				'<li class="divider"></li>'+
				'<li><a ng-click="properties()">Properties</a></li>'+
				'</ul>';

				//sensor management menu
				var menuSensorManagement = '<ul id="contextMenuSensorManagement" class="dropdown-menu">'+
					'<li><a href ="#/sensorWizard/{{elementSelected.restProtocol}}/{{elementSelected.ipAddress}}/{{elementSelected.restPort}}">Add Sensor</a></li>'+
					'</ul>';

				//sensor menu
				var menuSensor = '<ul id="contextMenuSensor" class="dropdown-menu">'+
					'<li><a ng-click="deleteSensor()">Delete Sensor</a></li>'+
					'<li><a ng-click="createSession()">Create Session</a></li>'+
					'</ul>';

				//session menu
				var menuSession = '<ul id="contextMenuSession" class="dropdown-menu">'+
					'<li><a ng-click="startSession()">Start Session</a></li>'+
					'<li><a ng-click="deleteSession()">Delete Session</a></li>'+
					'</ul>';

				//servers menu
				var menuServers = '<ul id="contextMenuServers" class="dropdown-menu">'+
					'<li><a href ="#/serverWizard">Add server</a></li>'+
					'</ul>';

				//tree template
				var template =
					'<ul>' +
						'<li data-ng-repeat="node in ' + treeModel + '">' +
							'<i class="collapsed" data-ng-show="node.' + nodeChildren + '.length && node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node)"></i>' +
							'<i class="expanded" data-ng-show="node.' + nodeChildren + '.length && !node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node)"></i>' +
							'<i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i>' +
							'<i class="{{node.iconClass}}"></i> ' +
							'<span context="{{node.contextMenuId}}" data-ng-class="node.selected" data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>' +
							'<div  data-ng-hide="node.collapsed" data-tree-id="' + treeId + '" data-tree-model="node.' + nodeChildren + '" data-node-id=' + nodeId + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + '></div>' +
						'</li>' +
					'</ul>' +
						menuServer +
						menuSensorManagement +
						menuSensor +
						menuSession +
						menuServers
					;


				//check tree id, tree model
				if( treeId && treeModel ) {

					//root node
					if( attrs.angularTreeview ) {
					
						//create tree object if not exists
						scope[treeId] = scope[treeId] || {};

						//if node head clicks,
						scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function( selectedNode ){

							//Collapse or Expand
							selectedNode.collapsed = !selectedNode.collapsed;
						};

						//if node label clicks,
						scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function( selectedNode ){

							//remove highlight from previous node
							if( scope[treeId].currentNode && scope[treeId].currentNode.selected ) {
								scope[treeId].currentNode.selected = undefined;
							}

							//set highlight to selected node
							selectedNode.selected = 'selected';

							//set currentNode
							scope[treeId].currentNode = selectedNode;
						};
					}

					//Rendering template.
					element.html('').append( $compile( template )( scope ) );
				}
			}
		};
	}]);
})( angular );
